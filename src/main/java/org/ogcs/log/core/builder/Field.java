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

import org.ogcs.log.util.DataType;
import org.ogcs.log.util.MySQL;
import org.ogcs.utilities.StringUtil;

/**
 * MySQL table's field builder.
 *
 * @author TinyZ
 * @date 2016-07-01.
 * @since 1.0
 */
public class Field {

    /**
     * Field' name
     */
    private String name;
    /**
     * Data type.
     */
    private String type;
    /**
     * Data length info.
     * <p>
     * Notice: float(11,2)|decimal(11,2)
     */
    private String length;  //  special: float(11,2)|decimal(11,2)
    /**
     * Default data value.
     */
    private String defaultValue;
    /**
     * The field is primary key.
     */
    private boolean isPrimaryKey;
    /**
     * Is the fields 's value allow null.
     */
    private boolean isNotNull;
    /**
     * Is the fields 's value unsigned.  Only numeric.
     */
    private boolean isUnsigned;
    /**
     * Is auto increment
     */
    private boolean isAutoIncrement;
    /**
     * is Zero fill.
     * <p>
     * 使用之后数据库值被默认为Unsigned并且用0补齐数据位数   示例: 1(4) => 0001   215(7) => 0000215
     *
     * @deprecated 不推荐使用,
     */
    private boolean isZeroFill;
    /**
     * charset. Only string.
     */
    private String charset;
    /**
     * string collate. Only string.
     */
    private String collate;
    /**
     * The comment of field.
     */
    private String desc;

    @Deprecated
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

        if (!StringUtil.isEmpty(length)) {
            this.length = length;
        }
        if (!StringUtil.isEmpty(defaultValue)) {
            this.defaultValue = defaultValue;
        }
        this.isPrimaryKey = isPrimaryKey;
        this.isNotNull = isPrimaryKey || isNotNull;
        if (MySQL.isUnsigned(type)) {
            this.isUnsigned = isUnsigned;
        }
        if (MySQL.isAutoIncrement(type)) {
            this.isAutoIncrement = isAutoIncrement;
        }
        if (MySQL.isHasCharset(type)) {
            if (!StringUtil.isEmpty(charset)) {
                this.charset = charset;
            }
            if (!StringUtil.isEmpty(collate)) {
                this.collate = collate;
            }
        }
        if (!StringUtil.isEmpty(desc)) {
            this.desc = desc;
        }
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
