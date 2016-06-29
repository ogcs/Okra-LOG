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

import org.ogcs.log.mysql.DataType;

/**
 * MySQL table's field bean.
 *
 * @author TinyZ
 * @date 2015-04-29.
 * @since 1.0
 */
public class Field {

    /**
     * 字段名
     */
    private String name;
    /**
     * 数据类型
     */
    private String type;
    private String length;
    private String defaultValue;
    private boolean isPrimaryKey;
    private boolean isNotNull;
    private boolean isUnsigned;
    private boolean isAutoIncrement;
    /**
     * 是否用0补齐
     *
     * @deprecated 不推荐使用, 使用之后数据库值被默认为Unsigned并且用0补齐数据位数   示例: 1(4) => 0001   215(7) => 0000215
     */
    private boolean isZeroFill;
    private String charset;
    private String collate;
    private String desc;
    public static class Builder {
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

        public Builder name(String val)
        { name = val;         return this; }
        public Builder type(String val)
        { type = val;         return this; }
        public Builder length(String val)
        { length = val;         return this; }
        public Builder defaultValue(String val)
        { defaultValue = val;   return this; }
        public Builder isPrimaryKey(boolean val)
        { isPrimaryKey = val;   return this; }
        public Builder isNotNull(boolean val)
        { isNotNull = val;      return this; }
        public Builder isUnsigned(boolean val)
        { isUnsigned = val;     return this; }
        public Builder isAutoIncrement(boolean val)
        { isAutoIncrement = val;return this; }
        public Builder isZeroFill(boolean val)
        { isZeroFill = val;     return this; }
        public Builder charset(String val)
        { charset = val;        return this; }
        public Builder collate(String val)
        { collate = val;        return this; }
        public Builder desc(String val)
        { desc = val;           return this; }

        public Field build() {
            return new Field(this);
        }
    }
    public Field(Builder builder) {
        if (builder.name == null) {
            throw new NullPointerException("builder.name");
        }
        if (builder.type == null) {
            throw new NullPointerException("builder.type");
        }
        this.name = builder.name;
        this.type = builder.type;
        if (!StringUtil.isEmpty(builder.length)) {
            this.length = builder.length;
        }
        if (!StringUtil.isEmpty(builder.defaultValue)) {
            this.defaultValue = builder.defaultValue;
        }
        this.isPrimaryKey = builder.isPrimaryKey;
        this.isNotNull = builder.isPrimaryKey || builder.isNotNull;
        if (verify(DataType.Codes.UNSIGNED)) {
            this.isUnsigned = builder.isUnsigned;
        }
        if (verify(DataType.Codes.AUTO_INCREMENT)) {
            this.isAutoIncrement = builder.isAutoIncrement;
        }
        if (!StringUtil.isEmpty(builder.charset) && verify(DataType.Codes.HAS_CHARSET)) {
            this.charset = builder.charset;
        }
        if (!StringUtil.isEmpty(builder.collate) && verify(DataType.Codes.HAS_COLLATE)) {
            this.collate = builder.collate;
        }
        if (!StringUtil.isEmpty(builder.desc)) {
            this.desc = builder.desc;
        }
    }

    /**
     * Verify field attributes is valid.
     *
     * @param code attribute code
     * @return Return true if this field can set this attribute value, false otherwise.
     */
    public boolean verify(int code) {
        return DataType.verify(this.type, code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setIsNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setIsUnsigned(boolean isUnsigned) {
        this.isUnsigned = isUnsigned;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public boolean isZeroFill() {
        return isZeroFill;
    }

    public void setIsZeroFill(boolean isZeroFill) {
        this.isZeroFill = isZeroFill;
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
}
