package org.ogcs.log.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ogcs.log.core.builder.Field;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.core.parser.Dom4JParser;
import org.ogcs.log.util.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author TinyZ
 * @date 2016-10-12.
 */
public class MySQLTest {

    private String jdbcUrl;
    private String user;
    private String psw;
    private String database;

    private String xmlFilePath;
    private String testTableName;

    @Before
    public void setUp() throws Exception {
        this.jdbcUrl = "jdbc:mysql://127.0.0.1:3306/okra_log?&useUnicode=true&characterEncoding=UTF8&useSSL=false";
        this.user = "root";
        this.psw = "wooduan";
        this.database = "okra_log";
        this.xmlFilePath = "./config/aolog.xml";
        this.testTableName = "log_money";
    }

    /**
     * 测试从数据库提取表结构
     *
     * @throws SQLException
     */
    @Test
    public void testNewTable() throws SQLException {
//        Table<Field> table = MySQL.newTable(root, "okra_log", "log_money");
        Table<Field> tableTeam = MySQL.newTable(getConnection(), "tinyzzh", "t_team");

        System.out.println();
    }

    /**
     * 测试生成创建表的SQL
     *
     * @throws SQLException
     */
    @Test
    public void testCreateTableSQL() throws SQLException {
        Dom4JParser parser = new Dom4JParser(this.xmlFilePath);
        Table t_log_money = parser.getTable(testTableName);
        String tableSQL = MySQL.createTableSQL(t_log_money);
        try {
            Assert.assertEquals(exec(tableSQL), false);
        } finally {
            dropTable(t_log_money.name());
        }
    }

    public void dropTable(String tableName) {
        exec("DROP TABLE " + tableName);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.jdbcUrl, this.user, this.psw);
    }

    private boolean exec(String sql) {
        try (Connection conn = getConnection(); Statement stat = conn.createStatement()) {
            return stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
