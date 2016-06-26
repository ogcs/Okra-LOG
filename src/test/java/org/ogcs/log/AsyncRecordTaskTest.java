package org.ogcs.log;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.ogcs.log.mysql.Table;
import org.ogcs.log.parser.W3cDomParser;
import org.ogcs.utilities.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TinyZ
 * @date 2016/6/26.
 */
public class AsyncRecordTaskTest {

    public static AsyncRecordTask task;

    static {
        // datasource
        HikariConfig config = new HikariConfig();
//        config.setDriverClassName("");
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/eos");
        config.setUsername("root");
        config.setPassword("123456");
        HikariDataSource dataSource = new HikariDataSource(config);
        // xml
        W3cDomParser w3c = new W3cDomParser("conf/aolog.xml");
        Table logMoney = w3c.getTable("log_money");

        String str = "log_money|2016-06-24|openid|0|105|15|100|1000";
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(StringUtil.split(str, '|'));
        }

        task = new AsyncRecordTask(dataSource, logMoney, list);
    }

    @Test
    public void record() throws Exception {
        task.record();
    }
}