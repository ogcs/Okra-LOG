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

package org.ogcs.log;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ogcs.log.config.OkraConfig;
import org.ogcs.log.disruptor.LogRecordTask;
import org.ogcs.log.disruptor.OkraLogRecordEvent;
import org.ogcs.log.disruptor.OkraLogRecordEventHandler;
import org.ogcs.log.parser.Field;
import org.ogcs.log.parser.StructParser;
import org.ogcs.log.parser.Table;
import org.ogcs.log.parser.W3cDomParser;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lmax.disruptor.dsl.ProducerType.MULTI;
import static org.ogcs.log.disruptor.OkraLogRecordEvent.DEF_EVENT_FACTORY;

/**
 * @author TinyZ
 * @date 2016-07-06.
 */
public class MissionBoard {

    private static final int DEF_BUFFER_SIZE = 16;
    private static final ExecutorService DEFAULT_POOL = Executors.newCachedThreadPool();

    private OkraConfig config;
    private Map<String, Struct> board;
    private Disruptor<OkraLogRecordEvent> disruptor;
    private DataSource dataSource;
    private StructParser<Table> parser;
    private double version;

    public MissionBoard() {
        //  Disruptor
        this.disruptor = new Disruptor<>(DEF_EVENT_FACTORY, DEF_BUFFER_SIZE, DEFAULT_POOL, MULTI, new BlockingWaitStrategy());
        this.disruptor.handleEventsWith(new OkraLogRecordEventHandler());
//        this.disruptor.handleEventsWithWorkerPool(); // TODO: 用于后期EventHandler和WorkPool的WorkHandler对比测试
//        this.disruptor.handleExceptionsWith(exceptionHandler);
        this.disruptor.start();

        this.board = new ConcurrentHashMap<>();
    }

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
        this.disruptor = new Disruptor<>(DEF_EVENT_FACTORY, rbSize, DEFAULT_POOL, MULTI, new BlockingWaitStrategy());
        this.disruptor.handleEventsWith(new OkraLogRecordEventHandler());
//        this.disruptor.handleEventsWithWorkerPool(); // TODO: 用于后期EventHandler和WorkPool的WorkHandler对比测试
//        this.disruptor.handleExceptionsWith(exceptionHandler);
        this.disruptor.start();
        //  Struct parser
        this.parser = new W3cDomParser(config.getLogPath());
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

    public void publish(LogRecordTask task) {
        RingBuffer<OkraLogRecordEvent> rb = disruptor.getRingBuffer();
        long next = rb.next();
        try {
            OkraLogRecordEvent event = rb.get(next);
            event.setValues(task);
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
            return DriverManager.getConnection(config.getDatabaseUrl(), config.getDatabaseUsername(), config.getDatabasePassword());
        }
        return dataSource.getConnection();
    }

    public StructParser<Table> getParser() {
        return parser;
    }
}
