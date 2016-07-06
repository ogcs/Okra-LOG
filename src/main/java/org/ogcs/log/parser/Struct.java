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

package org.ogcs.log.parser;

import org.ogcs.log.LogRecordTask;
import org.ogcs.log.NoticeBoard;
import org.ogcs.log.exception.IllegalVersionException;
import org.ogcs.log.util.MySQL;
import org.ogcs.utilities.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 结构体. 用于保存Table的版本信息和预查询语句
 *
 * @author TinyZ
 * @date 2016-07-01.
 */
public class Struct {

    /**
     * MySQL table bean.
     */
    private Table table;
    /**
     * The table prepare query sql.
     */
    private String prepareQuery;
    /**
     * The table's version. use to check and update table
     */
    private long version;

    private AtomicLong logsSize = new AtomicLong(0);

    private NoticeBoard board;

    private Queue<String[]> logs;

    private int limit;

    public Struct() {
        this.limit = 100;
        this.logs = new ConcurrentLinkedQueue<>();
    }

    /**
     * Update struct
     *
     * @param table   MySQL table.
     * @param version The struct's version.
     * @throws IllegalVersionException
     */
    public synchronized void update(Table table, long version) throws IllegalVersionException {
        if (table == null) throw new NullPointerException("table");
        if (version <= 0 || version <= this.version) {
            throw new IllegalVersionException("Current version : " + this.version + ", Target : " + version);
        }
        this.table = table;
        this.prepareQuery = MySQL.prepareQuery(table);
        if (StringUtil.isEmpty(this.prepareQuery)) {
            throw new IllegalStateException("prepareQuery is empty.");
        }
        this.version = version;
    }

    public void add(String[] params) {
        logs.add(params);
        long count = logsSize.incrementAndGet();
        if (count >= limit) {
            record(limit);
        }
    }

    public void record(int limit) {
        List<String[]> list = new ArrayList<>();
        while (list.size() < limit) {
            String[] poll = logs.poll();
            if (poll == null) {
                break;
            }
            list.add(poll);
        }
        if (!list.isEmpty()) {
            // TODO: 提交落地任务
            LogRecordTask task = new LogRecordTask(null, this, list);

        }
    }

    public void recordAll() {
        List<String[]> list = new ArrayList<>();
        String[] params;
        while ((params = logs.poll()) != null) {
            list.add(params);
        }
        if (!list.isEmpty()) {
            // TODO: 提交落地任务
            LogRecordTask task = new LogRecordTask(null, this, list);

        }
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
