/*
 *     Copyright 2016-2026 TinyZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ogcs.log.core;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.config.OkraConfig;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.core.handler.LogRecordTask;
import org.ogcs.log.core.handler.LogRecordTaskHandler;
import org.ogcs.log.core.parser.StructParser;
import org.ogcs.log.core.parser.W3cDomParser;
import org.ogcs.service.SimpleTaskService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.lmax.disruptor.dsl.ProducerType.MULTI;
import static org.ogcs.log.core.handler.LogRecordTaskFactory.DEFAULT_FACTORY;

/**
 * 任务版.
 * <p>
 * 客户端上报日志到任务版. 服务端队列形式保存日志, 累计一定数量的日志触发批量写入事件, 提交记录任务到Disruptor，写入MySQL数据库.
 *
 * @author TinyZ
 * @date 2016-07-06.
 */
public class MissionBoard {

    private static final Logger LOG = LogManager.getLogger(MissionBoard.class);

    private static final int DEF_BUFFER_SIZE = 16;
    private static final ExecutorService DEFAULT_POOL = Executors.newCachedThreadPool();

    private OkraConfig config;
    private Map<String, Struct> board;
    private Disruptor<LogRecordTask> disruptor;
    private DataSource dataSource;
    private StructParser<Table> parser;
    private SimpleTaskService tasks;
    private ScheduledFuture<?> future;
    private double version;

    public MissionBoard(OkraConfig config) {
        if (config == null) throw new NullPointerException("config");
        this.config = config;
        init();
    }

    public void init() {
        this.board = new ConcurrentHashMap<>();
        //  HikariCP
        HikariConfig hikariConfig = new HikariConfig(config.getHikariCPConfigPath());
        this.dataSource = new HikariDataSource(hikariConfig);
        //  Disruptor
        int rbSize = (config.getRingBufferSize() % 2 == 0 && config.getRingBufferSize() > 0) ? config.getRingBufferSize() : DEF_BUFFER_SIZE;
        this.disruptor = new Disruptor<>(DEFAULT_FACTORY, rbSize, DEFAULT_POOL, MULTI, new BlockingWaitStrategy());
        this.disruptor.handleEventsWith(new LogRecordTaskHandler());
//        this.disruptor.handleEventsWithWorkerPool(); // TODO: 用于后期EventHandler和WorkPool的WorkHandler对比测试
//        this.disruptor.handleExceptionsWith(exceptionHandler);
        this.disruptor.start();
        //  Struct parser
        this.parser = new W3cDomParser(config.getLogPath());
        this.tasks = new SimpleTaskService();

        // schedule publish task
        this.future = this.tasks.scheduleAtFixedRate(() -> {
            try {
                publishAll();
            } catch (Exception e) {
                LOG.error("Error publishAll().", e);
            }
        }, 1000L, config.getTaskInterval(), TimeUnit.MILLISECONDS);
        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
        }));
    }

    public void add(String tableName, String[] params) {
        Struct struct = board.get(tableName);
        if (struct == null) {
            Table table = this.parser.getTable(tableName);
            struct = new Struct(table, this);
            board.put(tableName, struct);
        }
        struct.add(params);
    }

    public void publish(Struct struct, List<String[]> list) {
        RingBuffer<LogRecordTask> rb = disruptor.getRingBuffer();
        long next = rb.next();
        try {
            LogRecordTask event = rb.get(next);
            event.setValues(struct, list);
        } finally {
            rb.publish(next);
        }
    }

    public void publishAll() {
        board.values().forEach(Struct::recordAll);
    }

    public void stop() {
        publishAll();
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            return DriverManager.getConnection(config.getDbJdbcUrl(), config.getDbUsername(), config.getDbPassword());
        }
        return dataSource.getConnection();
    }

    public StructParser<Table> getParser() {
        return parser;
    }
}
