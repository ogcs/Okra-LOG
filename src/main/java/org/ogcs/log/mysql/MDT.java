package org.ogcs.log.mysql;

/**
 *
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
