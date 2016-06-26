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

package org.ogcs.log.mysql;

import org.ogcs.log.parser.Field;
import org.ogcs.log.parser.Table;
import org.ogcs.utilities.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MySQL Database Utility.
 *
 * @author TinyZ
 * @since 1.0
 */
public final class MySQL2 {

    /**
     * Get Table from database
     *
     * @param statement The {@link Statement}
     * @param database  The database name
     * @param tableName The table name
     * @return Return {@link Table} instance is the table exist, null otherwise.
     * @throws SQLException
     */
    public static Table<Field> createTableBySql(Statement statement, String database, String tableName) throws SQLException {
        if (statement == null) {
            throw new NullPointerException("statement");
        }
        if (tableName == null) {
            throw new NullPointerException("tableName");
        }
        String sqlShowTableStatus = MySQL2.sqlShowTableStatus(database, tableName);
        String sqlShowFullField = MySQL2.sqlShowFullField(database, tableName);
        statement.execute(sqlShowTableStatus);
// 获取字段名
//        ResultSetMetaData metaData = statement.getResultSet().getMetaData();
//        System.out.println(metaData.getColumnCount());
//        for (int i = 0; i < metaData.getColumnCount(); i++) {
//            String columnName = metaData.getColumnName(i + 1);
//            System.out.println(columnName);
//        }
        Table<Field> table = null;
        ResultSet tableSet = statement.getResultSet();
        if (tableSet.first()) {
            table = new Table<Field>();
            // 1. Table information
            table.setName(tableSet.getString("Name"));
            table.setDbEngine(tableSet.getString("Engine"));
            String Auto_increment = tableSet.getString("Auto_increment");
            table.setAutoIncrement(StringUtil.isEmpty(Auto_increment) ? 0 : Integer.valueOf(Auto_increment));
            table.setCollate(tableSet.getString("Collation"));
            table.setDesc(tableSet.getString("Comment"));
            // 2. Field information
            statement.execute(sqlShowFullField);
            ResultSet fieldSet = statement.getResultSet();
            if (fieldSet != null) {
                Field[] fields = statFields(fieldSet);
                if (fields == null) {
                    throw new NullPointerException("statFields");
                }
                table.setFields(fields);
            }
        }
        return table;
    }

    public static Field[] statFields(ResultSet fieldSet) throws SQLException {
        List<Field> list = null;
        while (fieldSet.next()) {
            list = new ArrayList<>();
            Field field = new Field();
            field.setName(fieldSet.getString("Field"));
            String type = fieldSet.getString("Type");
            if (DataType.verify(type, DataType.Codes.DATE_TYPE)) {
                field.setType(type);
            } else {
                int startIndex = type.indexOf("(");
                int endIndex = type.indexOf(")");
                if (endIndex == type.length() - 1) {
                    field.setType(type.substring(0, startIndex));
                    field.setLength(type.substring(startIndex + 1, endIndex));
                } else {
                    String[] split = type.split(" ");
                    for (int i = 0; i < split.length; i++) {
                        if (i == 0) {
                            field.setType(type.substring(0, startIndex));
                            field.setLength(type.substring(startIndex + 1, endIndex));
                        } else if (split[i].equals("unsigned")) {
                            field.setIsUnsigned(true);
                        } else if (split[i].equals("zerofill")) {
                            field.setIsZeroFill(true);
                        }
                    }
                }
            }
            String collation = fieldSet.getString("Collation");
            if (collation != null) {
                field.setCollate(collation);
            }
            String Default = fieldSet.getString("Default");
            if (Default != null) {
                field.setDefaultValue(Default);
            }
            String Comment = fieldSet.getString("Comment");
            if (Comment != null) {
                field.setDesc(Comment);
            }
            boolean isNull = fieldSet.getBoolean("Null");
            field.setIsNotNull(!isNull);
            // TODO:索引 - 复杂部分暂时不处理, 只判断是否是主键索引
            String key = fieldSet.getString("Key");
            if (key.equals("PRI")) {
                field.setIsPrimaryKey(true);
            }
            String extra = fieldSet.getString("Extra");
            if (!StringUtil.isEmpty(extra)) {
                if (extra.equals("auto_increment")) {
                    field.setIsAutoIncrement(true);
                }
            }
            list.add(field);
        }
        if (list != null && list.size() > 0) {
            Field[] fields = new Field[list.size()];
            list.toArray(fields);
            return fields;
        }
        return null;
    }

    public static String sqlShowTableStatus(String database, String tableName) {
        if (StringUtil.isEmpty(database)) {
            return StringUtil.concatenate("SHOW TABLE STATUS FROM `", database, "` LIKE '", tableName, "';");
        } else {
            return StringUtil.concatenate("SHOW TABLE STATUS LIKE '", tableName, "';");
        }
    }

    public static String sqlShowFullField(String database, String tableName) {
        return StringUtil.concatenate("SHOW FULL FIELDS FROM ", tableName(database, tableName), ";");
    }

    public static String sqlInsert(Table table, String[] split) {
        if (verifyTableValid(table)) {
            String tableName = split[0];
            Field[] fields = table.getFields();
            if (tableName.equals(table.getName()) && fields.length == split.length - 1) {
                StringBuilder valuesBuilder = new StringBuilder();
                StringBuilder columnBuilder = new StringBuilder();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAutoIncrement()) {
                        continue;
                    }
                    columnBuilder.append("`").append(field.getName()).append("`");
                    // 第三版
                    String dataType = field.getType();
                    valuesBuilder.append(
                            ("NULL".equals(split[i + 1].toUpperCase()) || "".equals(split[i + 1])) ?
                                    !StringUtil.isEmpty(field.getDefaultValue()) ? numericOrString(dataType, field.getDefaultValue()) :
                                            (field.isPrimaryKey() || field.isNotNull()) ? numericOrString(dataType, "") : "NULL"
                                    : numericOrString(dataType, split[i + 1])
                    );
                    if (i != fields.length - 1) {
                        columnBuilder.append(",");
                        valuesBuilder.append(",");
                    }
                }
                if (columnBuilder.length() > 0) {
                    columnBuilder.insert(0, " (").append(")");
                }
                if (valuesBuilder.length() > 0) {
                    valuesBuilder.insert(0, " VALUES (").append(")");
                    return String.valueOf(new StringBuilder("INSERT INTO ")
                            .append(tableName(table.getDatabase(), table.getName()))
                            .append(columnBuilder)
                            .append(valuesBuilder)
                            .append(";"));
                }
            }
        }
        return null;
    }

    public static String sqlInsert(Table table, String data) {
        if (verifyTableValid(table)) {
            String[] split = data.split("\\|");
            String tableName = split[0];
            Field[] fields = table.getFields();
            if (tableName.equals(table.getName()) && fields.length == split.length - 1) {
                StringBuilder valuesBuilder = new StringBuilder();
                StringBuilder columnBuilder = new StringBuilder();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAutoIncrement()) {
                        continue;
                    }
                    columnBuilder.append("`").append(field.getName()).append("`");
                    // 第三版
                    String dataType = field.getType();
                    valuesBuilder.append(
                            ("NULL".equals(split[i + 1].toUpperCase()) || "".equals(split[i + 1])) ?
                                    !StringUtil.isEmpty(field.getDefaultValue()) ? numericOrString(dataType, field.getDefaultValue()) :
                                            (field.isPrimaryKey() || field.isNotNull()) ? numericOrString(dataType, "") : "NULL"
                                    : numericOrString(dataType, split[i + 1])
                    );
                    if (i != fields.length - 1) {
                        columnBuilder.append(",");
                        valuesBuilder.append(",");
                    }
                }
                if (columnBuilder.length() > 0) {
                    columnBuilder.insert(0, " (").append(")");
                }
                if (valuesBuilder.length() > 0) {
                    valuesBuilder.insert(0, " VALUES (").append(")");
                    return String.valueOf(new StringBuilder("INSERT INTO ")
                            .append(tableName(table.getDatabase(), table.getName()))
                            .append(columnBuilder)
                            .append(valuesBuilder)
                            .append(";"));
                }
            }
        }
        return null;
    }

    /*  第二版
     if (DataType.verify(field.getType(), DataType.Codes.NUMERIC_TYPE)) {
         sqlValues += "".equals(split[i + 1]) ?
                 isValidString(field.getDefaultValue()) ? field.getDefaultValue() :
                         (field.isPrimaryKey() || field.isNotNull()) ? 0 : "NULL" : split[i + 1];
     } else {
         sqlValues += ("NULL".equals(split[i + 1].toUpperCase()) || "".equals(split[i + 1])) ?
                 isValidString(field.getDefaultValue()) ? "'" + field.getDefaultValue() + "'" :
                         (field.isPrimaryKey() || field.isNotNull()) ? "''" : "NULL"
                 : "'" + split[i + 1] + "'";
     }

      第一版
     if (field.isPrimaryKey() || field.isNotNull()) {
         if (DataType.verify(field.getType(), DataType.Codes.NUMERIC_TYPE)) {
             sqlValues += "".equals(split[i + 1]) ? isValidString(field.getDefaultValue()) ? field.getDefaultValue() : 0 : split[i + 1];
         } else {
             if ("NULL".equals(split[i + 1].toUpperCase()) || "".equals(split[i + 1])) {
                 if (isValidString(field.getDefaultValue())) {
                     sqlValues += "'" + field.getDefaultValue() + "'";
                 } else {
                     sqlValues += "''";
                 }
             } else {
                 sqlValues += "'" + split[i + 1] + "'";
             }
         }
     } else {
         if (DataType.verify(field.getType(), DataType.Codes.NUMERIC_TYPE)) {
             sqlValues += "".equals(split[i + 1]) ? "NULL" : split[i + 1];
         } else {
             sqlValues += "NULL".equals(split[i + 1].toUpperCase()) ? "NULL" : "'" + split[i + 1] + "'";
         }
     }*/
    public static String numericOrString(String dataType, String value) {
        return DataType.verify(dataType, DataType.Codes.NUMERIC_TYPE) ? !StringUtil.isEmpty(value) ? value : "0" : "'" + value + "'";
    }

    public static void numericOrString(StringBuilder builder, String dataType, String value) {
        if (DataType.verify(dataType, DataType.Codes.NUMERIC_TYPE)) {
            builder.append(!StringUtil.isEmpty(value) ? value : "0");
        } else {
            builder.append("'").append(value).append("'");
        }
    }

    /**
     * Insert data without verify
     *
     * @param data The log data string
     * @return Return SQL if data parameter length > 1
     */
    @Deprecated
    public static String sqlInsertUnsafe(String data) {
        // Example:  INSERT INTO `black`.`s3` VALUES (NULL, 'Ken2', 'Still', 'Last', '1');
        final String[] split = data.split("\\|");
        if (split.length > 1) {
            String[] f = new String[split.length - 1];
            System.arraycopy(split, 1, f, 0, split.length - 1);
            return "INSERT INTO " + tableName(null, split[0]) + sqlPartialInsertValues(f);
        }
        return null;
    }

    public static String sqlPartialInsertValues(String[]... values) {
        if (values != null && values.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String[] value : values) {
                StringBuilder contentBuilder = new StringBuilder();
                for (int i = 0; i < value.length; i++) {
                    if (!("NULL".equals(value[i].toUpperCase()) || "".equals(value[i]))) {
                        contentBuilder.append("'").append(value[i]).append("'");
                    }
                    if (i == value.length - 1) {
                        contentBuilder.append(",");
                    }
                }
                builder.append(contentBuilder.insert(0, "(").append(")"));
            }
            if (builder.length() > 0) {
                return String.valueOf(builder.insert(0, "VALUES "));
            }
        }
        return null;
    }

    /**
     * Create sql to update table
     *
     * @param oldTable  The old table information
     * @param newTable  The new table information
     * @param isAddOnly Is create sql just add new field only. ignore the field attribute change
     * @return The update table sql
     */
    public static String sqlUpdateTable(Table oldTable, Table newTable, boolean isAddOnly) {
        // Example: ALTER TABLE `tiny`.`mem_sample1xy3`
//        CHANGE `xid` `xid` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT '字段ID',
//                CHANGE `xStr_name` `xStr_name` VARCHAR(50) CHARSET utf8 COLLATE utf8_general_ci NOT NULL,
//        CHANGE `xStr_2` `xStr_2` VARCHAR(50) CHARSET gb2312 COLLATE gb2312_chinese_ci NULL,
//                ADD COLUMN `xy1` INT(11) UNSIGNED NULL AFTER `xStr_2`,
//        DROP PRIMARY KEY,
//                ADD PRIMARY KEY (`xid`);
        if (!verifyTableValid(oldTable) || !verifyTableValid(newTable)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 0. header
        builder.append(" ALTER TABLE ");
        if (!StringUtil.isEmpty(oldTable.getDatabase())) {
            builder.append(" `").append(oldTable.getDatabase().toLowerCase()).append("`.`").append(oldTable.getName().toLowerCase()).append("` ");
        } else {
            builder.append(" `").append(oldTable.getName().toLowerCase()).append("` ");
        }
        // 1. column change or add
        StringBuilder contentBuilder = new StringBuilder();
        Field[] newFields = newTable.getFields();
        Field[] oldFields = oldTable.getFields();
        if (isAddOnly && newFields.length == oldFields.length) {
            return null;
        }
        boolean isPrimaryKeyChanged = false;
        List<String> keys = new ArrayList<>();
        int countOldPrimaryKey = 0;
        for (int i = 0; i < newFields.length; i++) {
            boolean isFieldChanged = false;
            Field newField = newFields[i];

            if (i < oldFields.length) {
                Field oldField = oldFields[i];
                if (oldField.isPrimaryKey()) {
                    countOldPrimaryKey++;
                }
                if (newField.isPrimaryKey() && oldField.isPrimaryKey()) {
                    keys.add(newField.getName());
                    isPrimaryKeyChanged = true;
                }
                // Change
                if (!isAddOnly && isFieldChanged(oldField, newField)) {
                    isFieldChanged = true;
                    contentBuilder.append(sqlFieldChange(oldField.getName(), newField));
                }
            } else {
                if (newField.isPrimaryKey()) {
                    keys.add(newField.getName());
                    isPrimaryKeyChanged = true;
                }
                isFieldChanged = true;
                contentBuilder.append(sqlFieldAddColumn(newFields[i - 1].getName(), newField));
            }
            if (isFieldChanged && !(i == newFields.length - 1 && !isPrimaryKeyChanged)) {
                contentBuilder.append(" , \n");
            }
        }
        if (isPrimaryKeyChanged) {
            // Example: DROP PRIMARY KEY, ADD PRIMARY KEY (`xid`, `xy1`),
            if (countOldPrimaryKey > 0) {
                contentBuilder.append(" DROP PRIMARY KEY, ");
            }
            contentBuilder.append(" ADD ");
            contentBuilder.append(sqlPrimaryKey(keys));
        }
        // 2. table attribute change
        if (contentBuilder.length() > 0) {
            contentBuilder.append(" , ");
        }
        if (!StringUtil.isEmpty(newTable.getDbEngine()) && !newTable.getDbEngine().equals(oldTable.getDbEngine())) {
            contentBuilder.append(" ENGINE=").append(newTable.getDbEngine()).append(" , ");
        }
        if (!StringUtil.isEmpty(newTable.getCharset()) && !newTable.getCharset().equals(oldTable.getCharset())) {
            contentBuilder.append(" CHARSET=").append(newTable.getCharset()).append(" , ");
        }
        if (!StringUtil.isEmpty(newTable.getCollate()) && !newTable.getCollate().equals(oldTable.getCollate())) {
            contentBuilder.append(" COLLATE=").append(newTable.getCollate()).append(" , ");
        }
        if (newTable.getAutoIncrement() > 0 && newTable.getAutoIncrement() != oldTable.getAutoIncrement()) {
            contentBuilder.append(" AUTO_INCREMENT=").append(String.valueOf(newTable.getAutoIncrement())).append(" , ");
        }
        if (!StringUtil.isEmpty(newTable.getDesc()) && !newTable.getDesc().equals(oldTable.getDesc())) {
            contentBuilder.append(" COMMENT='").append(newTable.getDesc()).append("' , ");
        }
        // remove the last ", " string to make the sql right
        if (contentBuilder.length() > 2) {
            contentBuilder.delete(contentBuilder.length() - 2, contentBuilder.length());
            return String.valueOf(builder.append(contentBuilder).append(";"));
        }
        // 3. table rename ()
        if (!oldTable.getName().toLowerCase().equals(newTable.getName().toLowerCase())) {
            String tableRename = sqlTableRename(newTable.getDatabase(), oldTable.getName(), newTable.getName());

        }
        return null;
    }

    /**
     * 获取创建数据库表的SQL
     *
     * @param table 表结构
     * @return 返回创建数据库表的SQL
     */
    public static String sqlCreateTable(Table table) {
        if (!verifyTableValid(table)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS `");
        if (!StringUtil.isEmpty(table.getDatabase())) {
            builder.append(table.getDatabase().toLowerCase()).append("`.`").append(table.getName().toLowerCase());
        } else {
            builder.append(table.getName().toLowerCase());
        }
        builder.append("` (\n");
        Field[] fields = table.getFields();
        StringBuilder priBuilder = null;
        for (int i = 0; i < fields.length; i++) {
            sqlFieldCreate(builder, fields[i]);
            if (i != fields.length - 1) {
                builder.append(", \n");
            }
            if (fields[i].isPrimaryKey()) {
                if (priBuilder == null) {
                    priBuilder = new StringBuilder();
                }
                priBuilder.append("`").append(fields[i].getName()).append("`,");
            }
        }
        if (priBuilder != null && priBuilder.length() > 0) {
            priBuilder.delete(priBuilder.length() - 1, priBuilder.length()).insert(0, ", \n PRIMARY KEY (").append(")");
            builder.append(priBuilder);
        }
        StringBuilder append = builder.append("\n)").append(sqlTableAttribute(table)).append(";");
        return String.valueOf(append);
    }

    /**
     * Check the new field is different from the old field
     *
     * @param oldField the old field
     * @param newField the new Field
     * @return Return true if the new Field is different from the old field
     */
    public static boolean isFieldChanged(Field oldField, Field newField) {
        boolean isChanged = !oldField.getName().equals(newField.getName());
        isChanged |= !oldField.getType().equals(newField.getType());
        if (!StringUtil.isEmpty(newField.getCharset())) {
            isChanged |= !newField.getCharset().toLowerCase().equals(oldField.getCharset().toLowerCase());
        }
        if (!StringUtil.isEmpty(newField.getCollate())) {
            isChanged |= !newField.getCollate().toLowerCase().equals(oldField.getCollate().toLowerCase());
        }
        if (!StringUtil.isEmpty(newField.getDefaultValue())) {
            isChanged |= !newField.getDefaultValue().equals(oldField.getDefaultValue());
        }
//        if (StringUtil.isValidString(newField.getLength())) {
//            isChanged |= !newField.getLength().toLowerCase().equals(oldField.getLength().toLowerCase());
//        }
        if (!StringUtil.isEmpty(newField.getLength())) {
            isChanged |= !newField.getLength().equals(oldField.getLength());
        }
        if (!StringUtil.isEmpty(newField.getDesc())) {
            isChanged |= !newField.getDesc().equals(oldField.getDesc());
        }
        isChanged |= oldField.isNotNull() != newField.isNotNull();
        isChanged |= oldField.isAutoIncrement() != newField.isAutoIncrement();
        isChanged |= oldField.isUnsigned() != newField.isUnsigned();
        //                oldField.isPrimaryKey() != newField.isPrimaryKey() ||
//                oldField.isZeroFill() != newField.isZeroFill()
        return isChanged;
    }

    public static StringBuilder sqlPrimaryKey(Collection<String> keys) {
        StringBuilder builder = new StringBuilder();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                if (builder.length() == 0) {
                    builder.append(" PRIMARY KEY (");
                } else {
                    builder.append(",");
                }
                builder.append("`").append(key).append("`");
            }
            if (builder.length() > 0) {
                builder.append(") ");
            }
        }
        return builder;
    }

    public static String sqlPrimaryKey(String... keys) {
        StringBuilder builder = new StringBuilder();
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                if (builder.length() == 0) {
                    builder.append(" PRIMARY KEY (");
                } else {
                    builder.append(",");
                }
                builder.append("`").append(key).append("`");
            }
            if (builder.length() > 0) {
                builder.append(") ");
            }
        }
        return String.valueOf(builder);
    }

    /**
     * table attribute sql
     *
     * @param table {@link Table}
     * @return return sql string
     */
    public static String sqlTableAttribute(Table table) {
        StringBuilder builder = new StringBuilder();
        if (!StringUtil.isEmpty(table.getDbEngine())) {
            builder.append(" ENGINE=").append(table.getDbEngine());
        }
        if (!StringUtil.isEmpty(table.getCharset())) {
            builder.append(" CHARSET=").append(table.getCharset());
        }
        if (!StringUtil.isEmpty(table.getCollate())) {
            builder.append(" COLLATE=").append(table.getCollate());
        }
        if (table.getAutoIncrement() > 0) {
            builder.append(" AUTO_INCREMENT=").append(String.valueOf(table.getAutoIncrement()));
        }
        if (!StringUtil.isEmpty(table.getDesc())) {
            builder.append(" COMMENT='").append(table.getDesc()).append("'");
        }
        return String.valueOf(builder);
    }

    public static String sqlTableRename(String database, String oldTableName, String newTableName) {
        return " RENAME TABLE " + MySQL2.tableName(database, oldTableName) + " TO " + MySQL2.tableName(database, newTableName);
    }

    /**
     * The table name.
     * <p>If the operation system is Linux/ CentOS 6.x. the mysql table name must be lower string</p>
     *
     * @param database  The database name
     * @param tableName The table name
     * @return Return the table name sql string
     */
    public static String tableName(String database, String tableName) {
        if (!StringUtil.isEmpty(database)) {
            // Example : `tiny`.`mem_sample1xy1`
            return StringUtil.concatenate(" `", database.toLowerCase(), "`.", "`", tableName.toLowerCase(), "` ");
        } else {
            // Example : `mem_sample1xy1`
            return StringUtil.concatenate(" `", tableName.toLowerCase(), "` ");
        }
    }

    public static String sqlFieldAddColumn(String lastColumnName, Field field) {
        // Example : ADD COLUMN `xy1` INT(11) UNSIGNED NOT NULL  COMMENT 'ds' AFTER `xStr_2`,
        StringBuilder builder = new StringBuilder();
        builder.append(" ADD COLUMN ");
        sqlFieldBase(builder, field);
        builder.append(" AFTER `").append(lastColumnName).append("`");
        return String.valueOf(builder);
    }

    /**
     * Change field attribute
     *
     * @param oldFieldName The old field name
     * @param field        {@link Field}
     * @return Return the change field SQL.
     * Example : CHANGE `xid` `xid1` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT  COMMENT '字段ID'
     */
    public static String sqlFieldChange(String oldFieldName, Field field) {
        StringBuilder builder = new StringBuilder();
        builder.append(" CHANGE `").append(oldFieldName).append("` ");
        return String.valueOf(sqlFieldBase(builder, field));
    }

    /**
     *
     * <pre>
     *     Example: `xStr_2` VARCHAR(50) CHARSET gb2312 COLLATE gb2312_chinese_ci NULL
     * </pre>
     * @param field
     * @return
     */
    public static String sqlFieldCreate(Field field) {
        return String.valueOf(sqlFieldBase(new StringBuilder(), field));
    }

    public static StringBuilder sqlFieldCreate(StringBuilder builder, Field field) {
        // Example: `xStr_2` VARCHAR(50) CHARSET gb2312 COLLATE gb2312_chinese_ci NULL
        return sqlFieldBase(builder, field);
    }

    public static StringBuilder sqlFieldBase(StringBuilder builder, Field field) {
        builder.append(" `").append(field.getName()).append("` ");
        builder.append(field.getType().toUpperCase());
        if (!StringUtil.isEmpty(field.getLength())) {
            builder.append(" (").append(field.getLength()).append(") ");
        }
        if (field.isUnsigned() && field.verify(DataType.Codes.UNSIGNED)) {
            builder.append(" UNSIGNED ");
        }
        if (field.isZeroFill() && field.verify(DataType.Codes.ZERO_FILL)) {
            builder.append(" ZEROFILL ");
        }
        if (!StringUtil.isEmpty(field.getCharset()) && field.verify(DataType.Codes.HAS_CHARSET)) {
            builder.append(" CHARSET ").append(field.getCharset());
        }
        if (!StringUtil.isEmpty(field.getCollate()) && field.verify(DataType.Codes.HAS_COLLATE)) {
            builder.append(" COLLATE ").append(field.getCollate());
        }
        if (field.isPrimaryKey() || field.isNotNull()) {
            builder.append(" NOT NULL ");
        } else {
            builder.append(" NULL ");
        }
        if (field.isAutoIncrement() && field.verify(DataType.Codes.AUTO_INCREMENT)) {
            builder.append(" AUTO_INCREMENT ");
        }
        if (!StringUtil.isEmpty(field.getDefaultValue()) && !field.isPrimaryKey()) {
            builder.append(" DEFAULT ").append(field.getDefaultValue());
        }
        if (!StringUtil.isEmpty(field.getDesc())) {
            builder.append(" COMMENT '").append(field.getDesc()).append("'");
        }
        return builder;
    }

    /**
     * Verify auto increment's fieldSQL count
     *
     * @return Return true if Table 's fieldSQL is valid and attribute is right.
     */
    public static boolean verifyTableValid(Table table) {
        if (table == null || null == table.getName() || "".equals(table.getName())) {
//            LOG.info("Parameter table is null or table's name is null.");
            return false;
        }
        Field[] fields = table.getFields();
        if (fields == null || fields.length <= 0) {
//            LOG.info("table's fields is null or size is zero");
            return false;
        }
        int autoIncrementCount = 0;
        for (Field field : fields) {
            if (field.isAutoIncrement()) {
                autoIncrementCount++;
            }
        }
        return autoIncrementCount <= 1;
    }
}
