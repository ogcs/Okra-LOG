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

package org.ogcs.log.parser;

/**
 * @author TinyZ
 * @date 2016-06-29.
 */
public class TableBuilder<F extends Field> implements Builder<Table<F>> {

    private String database;
    private String name;
    private String dbEngine;
    private String charset;
    private String collate;
    private String desc;
    private int autoIncrement = 1;
    private F[] fields;

    private String prepareQuery;
    private double version = 0.0;

    @Override
    public TableBuilder newBuilder() {
        return new TableBuilder<>();
    }

    @Override
    public Table<F> build() {
        return new Table<>(database)
                ;
    }

    public TableBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public TableBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TableBuilder setDbEngine(String dbEngine) {
        this.dbEngine = dbEngine;
        return this;
    }

    public TableBuilder setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public TableBuilder setCollate(String collate) {
        this.collate = collate;
        return this;
    }

    public TableBuilder setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public TableBuilder setAutoIncrement(int autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    public TableBuilder setFields(F[] fields) {
        this.fields = fields;
        return this;
    }

    public TableBuilder setPrepareQuery(String prepareQuery) {
        this.prepareQuery = prepareQuery;
        return this;
    }

    public TableBuilder setVersion(double version) {
        this.version = version;
        return this;
    }
}
