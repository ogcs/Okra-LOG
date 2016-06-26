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

    public Field() {
    }

    /**
     * Field constructor
     *
     * @param name the fieldSQL field's  name
     * @param type the data type. see {@link DataType}
     */
    public Field(String name, String type) {
        this(name, type, null, null, false, false, false, false, null, null, null);
    }

    /**
     * Field constructor
     *
     * @param name            the fieldSQL field's  name
     * @param type            the data type. see {@link DataType}
     * @param length          the data length attribute. (string type fieldSQL must set this attribute)
     * @param defaultValue    the data default value
     * @param isPrimaryKey    the fieldSQL is primary key
     * @param isNotNull       is the fieldSQL value not null. true is not null, false means value can be set NULL
     * @param isUnsigned      is numeric type fieldSQL value unsigned, true is unsigned, false otherwise
     * @param isAutoIncrement is numeric type fieldSQL value can auto increment
     * @param charset         string type fieldSQL 's charset
     * @param collate         string type fieldSQL 's collate
     * @param desc            other information
     */
    public Field(String name, String type, String length, String defaultValue, boolean isPrimaryKey, boolean isNotNull, boolean isUnsigned, boolean isAutoIncrement, String charset, String collate, String desc) {
        this.name = name;
        this.type = type;
        if (length != null && !"".equals(length)) {
            this.length = length;
        }
        if (defaultValue != null && !"".equals(defaultValue)) {
            this.defaultValue = defaultValue;
        }
        this.isPrimaryKey = isPrimaryKey;
        this.isNotNull = isPrimaryKey || isNotNull;
        if (verify(DataType.Codes.UNSIGNED)) {
            this.isUnsigned = isUnsigned;
        }
        if (verify(DataType.Codes.AUTO_INCREMENT)) {
            this.isAutoIncrement = isAutoIncrement;
        }
        this.isZeroFill = isZeroFill;
        if (charset != null && verify(DataType.Codes.HAS_CHARSET)) {
            this.charset = charset;
        }
        if (collate != null && verify(DataType.Codes.HAS_COLLATE)) {
            this.collate = collate;
        }
        if (desc != null && !"".equals(desc)) {
            this.desc = desc;
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
