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

/**
 * Mapping MySQL Data Types in Java.
 * (http://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html)
 *
 * @author TinyZ
 * @date 2016/6/25.
 */
public enum MDT {

    BIT,
    TINYINT,
    SMALLINT,
    MEDIUMINT,
    INT,
    INTEGER,
    BIGINT,
    FLOAT,
    DOUBLE,
    DECIMAL,

    DATE,
    TIME,
    YEAR,
    DATETIME,
    TIMESTAMP,

    CHAR,
    VARCHAR,
    TINYBLOB,
    TINYTEXT,
    BLOB,
    TEXT,
    MEDIUMBLOB,
    MEDIUMTEXT,
    LONGBLOB,
    LONGTEXT;


}
