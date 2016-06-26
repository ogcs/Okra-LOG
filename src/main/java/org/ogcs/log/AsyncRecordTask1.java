package org.ogcs.log;

import org.ogcs.log.mysql.Table;

import javax.sql.DataSource;

/**
 * @author TinyZ
 * @date 2016/6/25.
 */
public class AsyncRecordTask1 {

    private DataSource dataSource;
    private Table table;
    private String[] params;

    public AsyncRecordTask1(DataSource dataSource, Table table, String[] params) {
        this.dataSource = dataSource;
        this.table = table;
        this.params = params;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
