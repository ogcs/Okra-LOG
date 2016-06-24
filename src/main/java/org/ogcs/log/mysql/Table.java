package org.ogcs.log.mysql;

/**
 * MySQL table bean.
 *
 * @since 1.0
 */
public class Table<F extends Field> {

    private String database;
    private String name;
    private String dbEngine;
    private String charset;
    private String collate;
    private String desc;
    private int autoIncrement = 1;
    private F[] fields;

    /**
     * The table's version . use to check and update table
     */
    private double version = 0.0;

    public Table() {
    }

    public Table(String database) {
        this.database = database;
    }

    public double version() {
        return version;
    }

    public String getDatabase() {
        return database;
    }

    //  --------------- Can't change database -----------
//    public void setDatabase(String database) {
//        this.database = database;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbEngine() {
        return dbEngine;
    }

    public void setDbEngine(String dbEngine) {
        this.dbEngine = dbEngine;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(int autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public F[] getFields() {
        return fields;
    }

    public void setFields(F[] fields) {
        this.fields = fields;
    }

    /**
     * The method is replaced by version()
     *
     * @return Return the table version
     */
    @Deprecated
    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }
}
