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

package org.ogcs.log.util;

import java.util.HashMap;
import java.util.Map;

public final class DataType {

    // Numeric type
    public static final String MYSQL_BIT = "BIT";
    public static final String MYSQL_TINYINT = "TINYINT";
    public static final String MYSQL_SMALLINT = "SMALLINT";
    public static final String MYSQL_MEDIUMINT = "MEDIUMINT";
    public static final String MYSQL_INT = "INT";
    public static final String MYSQL_INTEGER = "INTEGER";
    public static final String MYSQL_BIGINT = "BIGINT";
    public static final String MYSQL_FLOAT = "FLOAT";
    public static final String MYSQL_DOUBLE = "DOUBLE";
    public static final String MYSQL_DECIMAL = "DECIMAL";
    // Date and time type
    public static final String MYSQL_DATE = "DATE";
    public static final String MYSQL_TIME = "TIME";
    public static final String MYSQL_YEAR = "YEAR";
    public static final String MYSQL_DATETIME = "DATETIME";
    public static final String MYSQL_TIMESTAMP = "TIMESTAMP";
    // String type
    public static final String MYSQL_CHAR = "CHAR";
    public static final String MYSQL_VARCHAR = "VARCHAR";
    public static final String MYSQL_TINYBLOB = "TINYBLOB";
    public static final String MYSQL_TINYTEXT = "TINYTEXT";
    public static final String MYSQL_BLOB = "BLOB";
    public static final String MYSQL_TEXT = "TEXT";
    public static final String MYSQL_MEDIUMBLOB = "MEDIUMBLOB";
    public static final String MYSQL_MEDIUMTEXT = "MEDIUMTEXT";
    public static final String MYSQL_LONGBLOB = "LONGBLOB";
    public static final String MYSQL_LONGTEXT = "LONGTEXT";
    private static final Map<String, Integer[]> DATA_TYPES = new HashMap<>();

    static {
        DATA_TYPES.put(MYSQL_BIT, new Integer[]{Codes.NUMERIC_TYPE});
        DATA_TYPES.put(MYSQL_TINYINT, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_SMALLINT, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_MEDIUMINT, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_INT, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_INTEGER, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_BIGINT, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_FLOAT, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_DOUBLE, new Integer[]{Codes.NUMERIC_TYPE, Codes.AUTO_INCREMENT, Codes.UNSIGNED, Codes.ZERO_FILL});
        DATA_TYPES.put(MYSQL_DECIMAL, new Integer[]{Codes.NUMERIC_TYPE, Codes.UNSIGNED});

        DATA_TYPES.put(MYSQL_DATE, new Integer[]{Codes.DATE_TYPE});
        DATA_TYPES.put(MYSQL_TIME, new Integer[]{Codes.DATE_TYPE});
        DATA_TYPES.put(MYSQL_YEAR, new Integer[]{Codes.DATE_TYPE});
        DATA_TYPES.put(MYSQL_DATETIME, new Integer[]{Codes.DATE_TYPE});
        DATA_TYPES.put(MYSQL_TIMESTAMP, new Integer[]{Codes.DATE_TYPE});

        DATA_TYPES.put(MYSQL_CHAR, new Integer[]{Codes.STRING_TYPE, Codes.HAS_CHARSET, Codes.HAS_COLLATE});
        DATA_TYPES.put(MYSQL_VARCHAR, new Integer[]{Codes.STRING_TYPE, Codes.HAS_CHARSET, Codes.HAS_COLLATE});
        DATA_TYPES.put(MYSQL_TINYBLOB, new Integer[]{Codes.STRING_TYPE});
        DATA_TYPES.put(MYSQL_TINYTEXT, new Integer[]{Codes.STRING_TYPE, Codes.HAS_CHARSET, Codes.HAS_COLLATE});
        DATA_TYPES.put(MYSQL_BLOB, new Integer[]{Codes.STRING_TYPE});
        DATA_TYPES.put(MYSQL_TEXT, new Integer[]{Codes.STRING_TYPE, Codes.HAS_CHARSET, Codes.HAS_COLLATE});
        DATA_TYPES.put(MYSQL_MEDIUMBLOB, new Integer[]{Codes.STRING_TYPE});
        DATA_TYPES.put(MYSQL_MEDIUMTEXT, new Integer[]{Codes.STRING_TYPE, Codes.HAS_CHARSET, Codes.HAS_COLLATE});
        DATA_TYPES.put(MYSQL_LONGBLOB, new Integer[]{Codes.STRING_TYPE});
        DATA_TYPES.put(MYSQL_LONGTEXT, new Integer[]{Codes.STRING_TYPE, Codes.HAS_CHARSET, Codes.HAS_COLLATE});
    }

    /**
     * Check the the field attributes
     *
     * @param dataType the field dataType attribute
     * @param code     attribute code
     * @return return true if {@link DataType} have this attribute , false otherwise.
     */
    public static boolean verify(String dataType, int code) {
        Integer[] attributes = DATA_TYPES.get(dataType.toUpperCase());
        if (attributes != null && attributes.length > 0) {
            for (int attribute : attributes) {
                if (code == attribute) {
                    return true;
                }
            }
        }
        return false;
    }

    public final class Codes {

        public static final int AUTO_INCREMENT = 0;
        public static final int UNSIGNED = 1;
        /**
         * Must have length attribute
         */
        public static final int MUST_LENGTH = 2;
        /**
         * Has charset attribute
         */
        public static final int HAS_CHARSET = 3;
        /**
         * Has collate attribute
         */
        public static final int HAS_COLLATE = 4;

        public static final int ZERO_FILL = 5;

        /**
         * numeric type
         */
        public static final int NUMERIC_TYPE = 6;

        /**
         * date and time type
         */
        public static final int DATE_TYPE = 7;

        /**
         * string type
         */
        public static final int STRING_TYPE = 8;
    }
}