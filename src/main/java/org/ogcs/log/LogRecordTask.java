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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.parser.Struct;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author TinyZ
 * @date 2016-06-25.
 */
public class LogRecordTask {

    private static final Logger LOG = LogManager.getLogger(LogRecordTask.class);
    private DataSource dataSource; // TODO: 改成数据源工具，可以按照策略选择数据源
    private Struct struct;
    private List<String[]> list;

    public LogRecordTask(DataSource dataSource, Struct struct, List<String[]> list) {
        this.dataSource = dataSource;
        this.struct = struct;
        this.list = list;
    }

    public void record() {
        if (dataSource == null) throw new NullPointerException("dataSource");
        if (struct == null) throw new NullPointerException("struct");
        if (list == null || list.isEmpty()) throw new IllegalStateException("list is Null or size is empty.");

        Connection con = null;
        PreparedStatement stat = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            String query = struct.getPrepareQuery();
            stat = con.prepareStatement(query);
            for (String[] params : list) {
                int len = Math.min(struct.getTable().getFields().length, params.length);
                for (int i = 1; i < len; i++) {
                    stat.setObject(i, params[i]);
                }
                stat.addBatch();
            }
            stat.executeBatch();
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
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
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                LOG.error("Database connection close error.", e);
            }
        }
    }
}
