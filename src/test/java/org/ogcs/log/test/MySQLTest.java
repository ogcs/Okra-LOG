package org.ogcs.log.test;

import org.junit.Test;
import org.ogcs.log.core.builder.Field;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.core.parser.Dom4JParser;
import org.ogcs.log.util.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author TinyZ
 * @date 2016-10-12.
 */
public class MySQLTest {

    /**
     * 测试从数据库提取表结构
     * @throws SQLException
     */
    @Test
    public void testNewTable() throws SQLException {
        String url ="jdbc:mysql://127.0.0.1:3306/tinyzzh?&useUnicode=true&characterEncoding=UTF8&useSSL=false";
        Connection root = DriverManager.getConnection(url, "root", "wooduan");
//        Table<Field> table = MySQL.newTable(root, "okra_log", "log_money");
        Table<Field> tableTeam = MySQL.newTable(root, "tinyzzh", "t_team");

        System.out.println();
    }

    /**
     * 测试生成创建表的SQL
     * @throws SQLException
     */
    @Test
    public void testCreateTableSQL() throws SQLException {

        Dom4JParser parser = new Dom4JParser("./config/aolog.xml");
        Table<Field> t_log_money = parser.getTable("log_money");

        String tableSQL = MySQL.createTableSQL(t_log_money);
        System.out.println(tableSQL);

    }
}
