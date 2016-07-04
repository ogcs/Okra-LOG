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

import org.ogcs.utilities.StringUtil;

/**
 * The {@link Field} builder.
 *
 * @author TinyZ
 * @since 1.0
 */
public final class FieldBuilder implements Builder<Field> {

    private String name;
    private String type;
    private String length;
    private String defaultValue;
    private boolean isPrimaryKey;
    private boolean isNotNull;
    private boolean isUnsigned;
    private boolean isAutoIncrement;
    private boolean isZeroFill;
    private String charset;
    private String collate;
    private String desc;

    private FieldBuilder() {
        //no-op
    }

    public static FieldBuilder newBuilder() {
        return new FieldBuilder();
    }

    @Override
    public Field build() {
        if (StringUtil.isEmpty(name)) throw new NullPointerException("name");
        if (StringUtil.isEmpty(type)) throw new NullPointerException("type");

        return new Field(name, type, length, defaultValue, isPrimaryKey, isNotNull, isUnsigned, isAutoIncrement, charset, collate, desc);
    }

    public FieldBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public FieldBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public FieldBuilder setLength(String length) {
        this.length = length;
        return this;
    }

    public FieldBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public FieldBuilder setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
        return this;
    }

    public FieldBuilder setNotNull(boolean notNull) {
        isNotNull = notNull;
        return this;
    }

    public FieldBuilder setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
        return this;
    }

    public FieldBuilder setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
        return this;
    }

    public FieldBuilder setZeroFill(boolean zeroFill) {
        isZeroFill = zeroFill;
        return this;
    }

    public FieldBuilder setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public FieldBuilder setCollate(String collate) {
        this.collate = collate;
        return this;
    }

    public FieldBuilder setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
