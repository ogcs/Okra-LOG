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

package org.ogcs.log.core.builder;

/**
 * MySQL table builder.
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

    @Deprecated
    public Table() {
    }

    /**
     * @param name   Database table's name.
     * @param fields table's fields array.
     * @param desc   Database table's description.
     */
    public Table(String name, F[] fields, String desc) {
        this(null, name, TableBuilder.DEFAULT_DB_ENGINE, TableBuilder.DEFAULT_CHARSET, TableBuilder.DEFAULT_COLLATE, desc, 0, fields);
    }

    public Table(String database, String name, F[] fields, String desc) {
        this(database, name, TableBuilder.DEFAULT_DB_ENGINE, TableBuilder.DEFAULT_CHARSET, TableBuilder.DEFAULT_COLLATE, desc, 0, fields);
    }

    public Table(String database, String name, String dbEngine, String charset, String collate, String desc, int autoIncrement, F[] fields) {
        this.database = database;
        this.name = name;
        this.dbEngine = dbEngine;
        this.charset = charset;
        this.collate = collate;
        this.desc = desc;
        this.autoIncrement = autoIncrement;
        this.fields = fields;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

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
}
