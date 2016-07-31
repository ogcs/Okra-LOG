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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.util.MySQL;
import org.ogcs.utilities.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 结构体. 处理Table的版本信息和预查询语句
 *
 * @author TinyZ
 * @date 2016-07-01.
 */
public class Struct {

    private static final Logger LOG = LogManager.getLogger(Struct.class);

    /**
     * MySQL table builder.
     */
    protected Table table;
    /**
     * The table prepare query sql.
     */
    protected String prepareQuery;
    /**
     * The mission board.
     */
    protected MissionBoard board;
    /**
     * The max threshold value of the logs queue's max length.
     */
    protected int threshold;
    /**
     * 每次批量写入数量
     */
    protected int batchCount;
    /**
     * 日志队列
     */
    protected Queue<String[]> logs;
    /**
     * 日志队列长度
     */
    protected AtomicLong logsSize = new AtomicLong(0);
    /**
     * 是否正在写入
     */
    private volatile boolean writing = false;

    public Struct(Table table, MissionBoard board) {
        if (table == null) throw new NullPointerException("table");
        if (board == null) throw new NullPointerException("board");
        this.board = board;
        this.batchCount = board.getConfig().getMaxBatchSize();

        this.table = table;
        this.prepareQuery = MySQL.prepareQuery(table);
        if (StringUtil.isEmpty(this.prepareQuery)) {
            throw new IllegalStateException("prepareQuery is empty.");
        }
        this.logs = newStructQueue();
    }

    /**
     * Update struct
     *
     * @param table MySQL table.
     */
    public synchronized void update(Table table) {
        if (table == null) throw new NullPointerException("table");
        this.table = table;
        this.prepareQuery = MySQL.prepareQuery(table);
        if (StringUtil.isEmpty(this.prepareQuery)) {
            throw new IllegalStateException("prepareQuery is empty.");
        }
    }

    /**
     * Add a log to queue.
     *
     * @param params
     */
    public void add(String[] params) {
        if (params == null) {
            return;
        }
        logs.add(params);
        long count = logsSize.incrementAndGet();
        if (count >= batchCount) {
            record(batchCount);
        }
    }

    public void addAll(final Collection<String[]> params) {
        if (params == null) {
            return;
        }
        final int paramsSize = params.size();
        if (paramsSize <= 0) {
            return;
        }
        long size = logsSize.get();
        if (size + paramsSize > threshold()) {
            if (LOG.isInfoEnabled()) {
                for (String[] param : params) {
                    LOG.info("Queue is full. drop log : " + StringUtil.implode(param, '|'));
                }
            }
            LOG.warn("Queue is full. drop extra elements.");
            return;
        }
        for (String[] param : params) {
            logs.add(param);
        }
        long length = logsSize.addAndGet(paramsSize);
        if (length >= batchCount) {
            record(batchCount);
        }
    }

    /**
     * Record all log in queue.
     */
    public void recordAll() {
        record(-1);
    }

    /**
     * Record special count log.
     * if batchCount number less than 0, will record all log.
     *
     * @param limit The record count.
     */
    public void record(int limit) {
        if (writing)
            return;
        writing = true;
        List<String[]> list = new ArrayList<>();
        String[] params;
        while ((params = logs.poll()) != null) {
            list.add(params);
            logsSize.decrementAndGet();
            if (limit > 0 && list.size() >= limit) {
                break;
            }
        }
        if (!list.isEmpty()) {
            board.publish(this, list);
        }
        writing = false;
    }

    /**
     * Create new log params queue.
     *
     * @return Return new {@link Queue}.
     */
    protected Queue<String[]> newStructQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    /**
     * Get The threshold value of the logs queue's max length.
     *
     * @return The threshold value of the logs queue's max length.
     */
    protected int threshold() {
        return threshold;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getPrepareQuery() {
        return prepareQuery;
    }

    public void setPrepareQuery(String prepareQuery) {
        this.prepareQuery = prepareQuery;
    }

    public MissionBoard getBoard() {
        return board;
    }

    public void setBoard(MissionBoard board) {
        this.board = board;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }
}
