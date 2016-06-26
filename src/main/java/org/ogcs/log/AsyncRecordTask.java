package org.ogcs.log;

import org.ogcs.log.mysql.Field;
import org.ogcs.log.mysql.Table;
import org.ogcs.log.util.MySQL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author TinyZ
 * @date 2016/6/25.
 */
public class AsyncRecordTask {

    private DataSource dataSource; // TODO: 改成数据源工具，可以按照策略选择数据源
    private Table table;
    private List<String[]> list;

    public AsyncRecordTask(DataSource dataSource, Table table, List<String[]> list) {
        this.dataSource = dataSource;
        this.table = table;
        this.list = list;
    }

    public void record() {
        if (dataSource == null) throw new NullPointerException("dataSource");
        if (table == null) throw new NullPointerException("table");
        if (list == null || list.isEmpty()) throw new IllegalStateException("list is Null or size is empty.");

        Field[] fields = table.getFields();
        Connection con = null;
        PreparedStatement stat = null;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            String query = MySQL.prepareQuery(table);
            stat = con.prepareStatement(query);
            for (String[] params : list) {
                // TODO: set参数值
                for (int i = 1; i < params.length; i++) {
                    String obj = params[i];
                    stat.setObject(i, params[i]);

//                    switch (bigint) {
//                        case BIT:
//                            stat.setBoolean(i, Boolean.valueOf(obj));
//                            break;
//                        case TINYINT:
//                            stat.setByte(i, Byte.valueOf(obj));
//                            break;
//                        case SMALLINT:
//                            stat.setShort(i, Short.valueOf(obj));
//                            break;
//                        case INT:
//                        case INTEGER:
//                            stat.setInt(i, Integer.valueOf(obj));
//                            break;
//                        case BIGINT:
//                            stat.setLong(i, Long.valueOf(obj));
//                            break;
//                        case FLOAT:
//                            stat.setFloat(i, Float.valueOf(obj));
//                            break;
//                        case DOUBLE:
//                            stat.setDouble(i, Double.valueOf(obj));
//                            break;
//                        case MEDIUMINT:
//                            break;
//                        case DECIMAL:
//                            break;
//                        case DATE:
//                            break;
//                        case TIME:
//                            break;
//                        case YEAR:
//                            break;
//                        case DATETIME:
//                            break;
//                        case TIMESTAMP:
//                            break;
//                        case CHAR:
//                        case VARCHAR:
//                            break;
//                        case TINYBLOB:
//                            break;
//                        case TINYTEXT:
//                            break;
//                        case BLOB:
//                            break;
//                        case TEXT:
//                            break;
//                        case MEDIUMBLOB:
//                            break;
//                        case MEDIUMTEXT:
//                            break;
//                        case LONGBLOB:
//                            break;
//                        case LONGTEXT:
//                            break;
//                        default:
//                            throw new IllegalStateException("Unknown Data Type : " + fields[i].getType());
//                    }
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
                e1.printStackTrace();
            }
            // TODO: SQL执行异常
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }


    }


}
