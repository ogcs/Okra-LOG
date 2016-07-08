package org.ogcs.log;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.ogcs.log.config.OkraConfig;
import org.ogcs.log.config.OkraProperties;
import org.ogcs.log.core.MissionBoard;
import org.ogcs.log.core.Struct;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.core.handler.LogRecordTask;
import org.ogcs.log.core.parser.W3cDomParser;
import org.ogcs.utilities.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TinyZ
 * @date 2016/6/26.
 */
public class LogRecordTaskTest {

    public static LogRecordTask task;

    static {
        OkraConfig config = OkraProperties.getConfig();

        // datasource
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getDbJdbcUrl());
        hikariConfig.setUsername(config.getDbUsername());
        hikariConfig.setPassword(config.getDbPassword());

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        // xml
        W3cDomParser parser = new W3cDomParser("conf/aolog.xml");
        Table logMoney = parser.getTable("log_money");
        //  log data
        String str = "log_money|2016-06-24|openid|0|105|15|100|1000";
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(StringUtil.split(str, '|'));
        }

        MissionBoard board = new MissionBoard(config);
        Struct struct = new Struct(logMoney, board);
        task = new LogRecordTask();
        task.setValues(struct, list);
    }

    @Test
    public void record() throws Exception {
        task.record();
    }
}