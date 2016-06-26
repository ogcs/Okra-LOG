package org.ogcs.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.parser.Table;
import org.ogcs.log.util.MySQL;

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
    private Table table;
    private List<String[]> list;

    public LogRecordTask(DataSource dataSource, Table table, List<String[]> list) {
        this.dataSource = dataSource;
        this.table = table;
        this.list = list;
    }

    public void record() {
        if (dataSource == null) throw new NullPointerException("dataSource");
        if (table == null) throw new NullPointerException("table");
        if (list == null || list.isEmpty()) throw new IllegalStateException("list is Null or size is empty.");

        Connection con = null;
        PreparedStatement stat = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            String query = MySQL.prepareQuery(table);
            stat = con.prepareStatement(query);
            for (String[] params : list) {
                for (int i = 1; i < params.length; i++) {
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
