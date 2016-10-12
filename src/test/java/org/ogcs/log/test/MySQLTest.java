package org.ogcs.log.test;

import org.ogcs.log.core.builder.Field;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.util.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author TinyZ
 * @date 2016-10-12.
 */
public class MySQLTest {


    public static void main(String[] args) throws SQLException {
        String url ="jdbc:mysql://127.0.0.1:3306/tiny?&useUnicode=true&characterEncoding=UTF8&useSSL=false";
        Connection root = DriverManager.getConnection(url, "root", "123456");
        Table<Field> table = MySQL.newTable(root, "okra_log", "log_money");

        System.out.println();
    }

    public void test() throws SQLException {

    }
}
