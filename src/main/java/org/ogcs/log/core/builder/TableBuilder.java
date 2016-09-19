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

import org.ogcs.utilities.StringUtil;

/**
 * The database {@link Table}'s builder
 *
 * @author TinyZ
 * @since 1.0.
 */
public class TableBuilder<F extends Field> implements Builder<Table<F>> {

    public static final String DEFAULT_DB_ENGINE = "InnoDB";
    public static final String DEFAULT_CHARSET = "utf8";
    public static final String DEFAULT_COLLATE = "utf8_general_ci";

    private String database;
    private String name;
    private String engine;
    private String charset;
    private String collate;
    private String desc;
    private int incr;
    private F[] fields;
    private KeyIndex[] indexes;

    private TableBuilder() {
        //no-op
    }

    public static <T extends Field> TableBuilder<T> newBuilder() {
        return new TableBuilder<>();
    }

    @Override
    public Table<F> build() {
        if (name == null) throw new NullPointerException("name");
        if (fields == null || fields.length <= 0) throw new NullPointerException("fields");
        if (StringUtil.isEmpty(engine))
            this.engine = DEFAULT_DB_ENGINE;
        if (StringUtil.isEmpty(charset))
            this.charset = DEFAULT_CHARSET;
        if (StringUtil.isEmpty(collate))
            this.collate = DEFAULT_COLLATE;
        incr = incr < 0 ? 0 : incr;
        return new Table<>(database, name, engine, charset, collate, desc, incr, fields);
    }

    public TableBuilder setDatabase(String database) {
        this.database = database;
        return this;
    }

    public TableBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TableBuilder setEngine(String engine) {
        this.engine = engine;
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

    public TableBuilder setIncr(int incr) {
        this.incr = incr;
        return this;
    }

    public TableBuilder setFields(F[] fields) {
        this.fields = fields;
        return this;
    }

    public TableBuilder setIndexes(KeyIndex[] indexes) {
        this.indexes = indexes;
        return this;
    }
}
