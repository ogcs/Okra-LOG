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

    /**
     * MySQL table builder.
     */
    private Table table;
    /**
     * The table prepare query sql.
     */
    private String prepareQuery;
    /**
     * The mission board.
     */
    private MissionBoard board;
    /**
     * 日志队列
     */
    private Queue<String[]> logs;

    private int limit;
    private AtomicLong logsSize = new AtomicLong(0);

    /**
     * 是否正在写入
     */
    private volatile boolean isWriting = false;

    public Struct(Table table, MissionBoard board) {
        if (table == null) throw new NullPointerException("table");
        if (board == null) throw new NullPointerException("board");
        this.board = board;
        this.limit = board.getConfig().getMaxBatchSize();

        this.table = table;
        this.prepareQuery = MySQL.prepareQuery(table);
        if (StringUtil.isEmpty(this.prepareQuery)) {
            throw new IllegalStateException("prepareQuery is empty.");
        }
        this.logs = new ConcurrentLinkedQueue<>();
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

    public void add(String[] params) {
        logs.add(params);
        long count = logsSize.incrementAndGet();
        if (count >= limit) {
            record(limit);
        }
    }

    public void addAll(Collection<String[]> params) {
        long count = 0;
        for (String[] param : params) {
            logs.add(param);
            count = logsSize.incrementAndGet();
        }
        if (count >= limit) {
            record(limit);
        }
    }

    public void addAll(String[]... params) {
        long count = 0;
        for (String[] param : params) {
            logs.add(param);
            count = logsSize.incrementAndGet();
        }
        if (count >= limit) {
            record(limit);
        }
    }

    public void record(int limit) {
        if (isWriting) {
            return;
        }
        isWriting = true;
        List<String[]> list = new ArrayList<>();
        while (list.size() < limit) {
            String[] poll = logs.poll();
            if (poll == null) {
                break;
            }
            list.add(poll);
            logsSize.decrementAndGet();
        }
        if (!list.isEmpty()) {
            board.publish(this, list);
        }
        isWriting = false;
    }

    public void recordAll() {
        if (isWriting) {
            return;
        }
        isWriting = true;
        List<String[]> list = new ArrayList<>();
        String[] params;
        while ((params = logs.poll()) != null) {
            list.add(params);
        }
        if (!list.isEmpty()) {
            board.publish(this, list);
        }
        isWriting = false;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
