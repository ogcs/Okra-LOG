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

package org.ogcs.log.core.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.app.Releasable;
import org.ogcs.log.core.Struct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Disruptor ringBuffer event.
 * 数据记录任务
 *
 * @author TinyZ.
 * @date 2016-07-08.
 */
public final class LogRecordTask implements Releasable {

    private static final Logger LOG = LogManager.getLogger(LogRecordTask.class);
    private Struct struct;
    private List<String[]> list;

    public LogRecordTask() {
        // no-op
    }

    public void setValues(Struct struct, List<String[]> list) {
        this.struct = struct;
        this.list = list;
    }

    /**
     * Write log to database.
     */
    public void record() {
        if (struct == null) throw new NullPointerException("struct");
        if (list == null || list.isEmpty()) throw new IllegalStateException("list is Null or size is empty.");
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = struct.getBoard().getConnection();
            conn.setAutoCommit(false);
            String query = struct.getPrepareQuery();
            stat = conn.prepareStatement(query);
            int filedSize = struct.getTable().getFields().length + 1; // 从1开始计数
            for (String[] params : list) {
                for (int i = 1; i < params.length; i++) {
                    stat.setObject(i, params[i]);
                }
                if (filedSize > params.length) { // 补全SQL中缺少的参数为null  // TODO: 当params少的时候，是否会出现问题？
                    for (int j = params.length; j < filedSize; j++) {
                        stat.setObject(j, null);
                    }
                }
//                int len = Math.min(struct.getTable().getFields().length, params.length);
//                for (int i = 1; i < len; i++) {
//                    stat.setObject(i, params[i]);
//                }
                stat.addBatch();
            }
            stat.executeBatch();
        } catch (SQLException e) {
            try {
                if (conn != null)
                    conn.rollback();
                struct.addAll(list);
            } catch (SQLException e1) {
                LOG.warn("Query rollback error.", e);
            }
            LOG.warn("SQL query error.", e);
        } catch (Exception e) {
            LOG.error("Log record logic error.", e);
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                LOG.error("Database connection close error.", e);
            }
            // release
            release();
        }
    }

    @Override
    public void release() {
        struct = null;
        if (list != null) {
            list.clear();
            list = null;
        }
    }
}
