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

package org.ogcs.log.serlvet.grafana;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.config.OkraConfig;
import org.ogcs.log.config.OkraProperties;
import org.ogcs.log.serlvet.AbstractApiServlet;
import org.ogcs.log.serlvet.grafana.bean.GfnMetricsQuery;
import org.ogcs.log.serlvet.grafana.bean.GfnQueryParam;
import org.ogcs.utilities.DateUtil;
import org.ogcs.utilities.StringUtil;
import org.ogcs.utilities.TimeV8Util;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grafana数据查询接口.
 * 用于Grafana的JsonDataSource插件查询数据.
 * 示例：
 * <pre>
 *  SELECT value1, time FROM tb_log_xxx WHERE <@timeFilter> GROUP BY FLOOR(UNIX_TIMESTAMP(logDate) * 1000 / <@timeInterval>);
 *  SELECT AVG(`value`) AS total, UNIX_TIMESTAMP(logDate) * 1000 AS `datetime` FROM log_money WHERE ioType=105 AND <@timeFilter> GROUP BY FLOOR(UNIX_TIMESTAMP(logDate) * 1000 / <@timeInterval>);|t105|2
 * <pre/>
 *
 * @author TinyZ
 * @date 2016-10-13.
 */
public class GfnQueryServlet extends AbstractApiServlet {

    private static final Logger LOG = LogManager.getLogger(GfnQueryServlet.class);

    public static void main(String[] args) throws SQLException {
//        long time = TimeV8Util.parseDateTime("2015-05-24T12:43:22.066Z", ISO_INSTANT);
//        LocalDateTime parse = LocalDateTime.parse("2015-05-24T12:43:22.066Z", ISO_INSTANT);//
        LocalDateTime parse2 = LocalDateTime.parse("2011-12-03T10:15:30.066Z", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));//
        long time = parse2.toEpochSecond(ZoneOffset.of("+08:00")) * 1000L;
        String s2 = DateUtil.formatTime(time);

        String url = "jdbc:mysql://127.0.0.1:3306/tinyzzh?&useUnicode=true&characterEncoding=UTF8&useSSL=false";
        Connection conn = DriverManager.getConnection(url, "root", "wooduan");
        String query = "SELECT AVG(`value`) AS total, UNIX_TIMESTAMP(logDate) * 1000 AS `datetime` FROM log_money WHERE ioType=105 GROUP BY FLOOR(UNIX_TIMESTAMP(logDate) * 1000 / 10000);";

        List<Map<String, List<String[]>>> list = new ArrayList<>();
        Map<String, List<String[]>> dataMap = new HashMap<>();
        Statement stat = conn.createStatement();
        stat.execute(query);
        ResultSet resultSet = stat.getResultSet();
        List<String[]> datapoints = new ArrayList<>();
        while (resultSet.next()) {
            String[] point = new String[]{resultSet.getString(1), resultSet.getString(2)};
            datapoints.add(point);
        }
        dataMap.put("aa", datapoints);
        list.add(dataMap);
        String content = JSON.toJSONString(list);

        System.out.println();
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public HttpResponse doGet(HttpRequest request) {
        if (!(request instanceof FullHttpRequest))
            return null;
        FullHttpRequest msg = (FullHttpRequest) request;
        String params = msg.content().toString(Charset.forName("UTF-8"));
        GfnQueryParam graQueryParam = JSON.parseObject(params, GfnQueryParam.class);
        //  config
        OkraConfig config = OkraProperties.getConfig();
        List<Map<String, Object>> datas = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(config.getDbJdbcUrl(), config.getDbUsername(), config.getDbPassword());
            for (GfnMetricsQuery gfnMetricsQuery : graQueryParam.getTargets()) {
                String[] split = gfnMetricsQuery.getTarget().split("\\|");
                if (split.length < 2) {
                    LOG.error("Query params error : " + params);
                    continue;
                }
                String target = split[1];
                int column = Integer.parseInt(split[2]);
                if (column < 3) {   //  Time series response
                    //  time filter
                    String sql = split[0].replace("<@timeFilter>",
                            StringUtil.format(" BETWEEN '{}' AND '{}' ",
                                    datetime(graQueryParam.getRange().getFrom()),
                                    datetime(graQueryParam.getRange().getTo()))
                    );
                    sql = sql.replace("<@timeInterval>", String.valueOf(timeInterval(graQueryParam.getInterval())));
                    //  query
                    Map<String, Object> dataMap = new HashMap<>();
                    Statement stat = conn.createStatement();
                    stat.execute(sql);
                    ResultSet resultSet = stat.getResultSet();
                    List<Object[]> datapoints = new ArrayList<>();
                    while (resultSet.next()) {
                        String[] point = new String[column];
                        for (int i = 1; i <= column; i++) {
                            point[i - 1] = resultSet.getString(i);
                        }
                        datapoints.add(point);
                    }
                    // response
                    dataMap.put("target", target);
                    dataMap.put("datapoints", datapoints);
                    datas.add(dataMap);
                } else {    //  Table response
                    //  TODO:   返回表单数据

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Query Data Error : ", e);
        }
        return response(JSON.toJSONString(datas));
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        return doGet(request);
    }

    private long time(String time) {
        return LocalDateTime
                .parse(time, FORMATTER)
                .toEpochSecond(ZoneOffset.of("+08:00")) * 1000L;
    }

    private String datetime(String time) {
        return TimeV8Util.dateTime(LocalDateTime.parse(time, FORMATTER));
    }

    private long timeInterval(String interval) {
        int index = interval.length() - 1;
        switch (interval.substring(index)) {
            case "s"://seconds
                return Long.valueOf(interval.substring(0, index)) * 1000L;
            case "m"://minutes
                return Long.valueOf(interval.substring(0, index)) * 60 * 1000L;
            case "h"://hours
                return Long.valueOf(interval.substring(0, index)) * 60 * 60 * 1000L;
            case "d"://days
                return Long.valueOf(interval.substring(0, index)) * 24 * 60 * 60 * 1000L;
            case "w"://weeks
                return Long.valueOf(interval.substring(0, index)) * 7 * 24 * 60 * 60 * 1000L;
            case "M"://months
                return Long.valueOf(interval.substring(0, index)) * 30 * 24 * 60 * 60 * 1000L;
            case "y"://years
                return Long.valueOf(interval.substring(0, index)) * 365 * 24 * 60 * 60 * 1000L;
            default:
                throw new IllegalStateException("Illegal interval param : " + interval);
        }
    }
}
