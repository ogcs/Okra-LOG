package org.ogcs.log.util;

import org.ogcs.log.mysql.DataType;
import org.ogcs.log.parser.Field;
import org.ogcs.log.parser.Table;
import org.ogcs.utilities.StringUtil;

import static org.ogcs.log.mysql.DataType.*;
import static org.ogcs.log.mysql.DataType.Codes.*;

/**
 * @author TinyZ
 * @date 2016/6/26.
 */
public final class MySQL {

    private MySQL() {
        //no-op
    }
    /**
     *
     * @param table
     * @return
     */
    public static String prepareQuery(Table table) {
        Field[] fields = table.getFields();
        StringBuilder sbValues = new StringBuilder();
        StringBuilder sbColumn = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAutoIncrement()) {
                continue;
            }
            sbColumn.append("`").append(field.getName()).append("`");
            sbValues.append("?");
            if (i != fields.length - 1) {
                sbColumn.append(",");
                sbValues.append(",");
            }
        }
        if (sbColumn.length() > 0) {
            sbColumn.insert(0, "(").append(")");
        }
        if (sbValues.length() > 0) {
            sbValues.insert(0, " (").append(")");
        }
        return new StringBuilder("INSERT INTO ")
                .append(tableName(table.getDatabase(), table.getName()))
                .append(sbColumn)
                .append(" VALUES ")
                .append(sbValues)
                .append(";")
                .toString();
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

    /**
     * The table name.
     * <p>If the operation system is Linux/ CentOS 6.x. the mysql table name must be lower string</p>
     *
     * <pre>
     *     Example : `database`.`table_name`
     *     Example : `table_name`
     * </pre>
     * @param database  The database name
     * @param tableName The table name
     * @return Return the table name sql string
     */
    public static String tableName(String database, String tableName) {
        if (StringUtil.isEmpty(database)) {
            return StringUtil.concatenate(" `", tableName.toLowerCase(), "` ");
        } else {
            return StringUtil.concatenate(" `", database.toLowerCase(), "`.", "`", tableName.toLowerCase(), "` ");
        }
    }

    /**
     * [MySQL] show table status.
     * <pre>
     *     Example : SHOW FULL FIELDS FROM `database`.`table_name`;
     * </pre>
     */
    public static String showTableStatusSQL(String database, String tableName) {
        if (StringUtil.isEmpty(database)) {
            return StringUtil.concatenate("SHOW TABLE STATUS FROM `", database, "` LIKE '", tableName, "';");
        } else {
            return StringUtil.concatenate("SHOW TABLE STATUS LIKE '", tableName, "';");
        }
    }

    /**
     * [MySQL] show full fields from table.
     * <pre>
     *     Example : SHOW FULL FIELDS FROM `database`.`table_name`;
     * </pre>
     */
    public static String showFullFieldSQL(String database, String tableName) {
        return StringUtil.concatenate("SHOW FULL FIELDS FROM ", tableName(database, tableName), ";");
    }

    public static String numericOrString(String dataType, String value) {
        return verify(dataType, NUMERIC_TYPE) ? StringUtil.isEmpty(value) ? "0" : value : "'" + value + "'";
    }

    public static void numericOrString(StringBuilder sb, String dataType, String value) {
        if (verify(dataType, NUMERIC_TYPE)) {
            sb.append(StringUtil.isEmpty(value) ? "0" : value);
        } else {
            sb.append("'").append(value).append("'");
        }
    }

    /**
     * 获取创建数据库表的SQL
     *
     * @param table 表结构
     * @return 返回创建数据库表的SQL
     */
    public static String createTableSQL(Table table) {
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

    public static StringBuilder sqlFieldCreate(StringBuilder builder, Field field) {
        // Example: `xStr_2` VARCHAR(50) CHARSET gb2312 COLLATE gb2312_chinese_ci NULL
        return sqlFieldBase(builder, field);
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
        return " RENAME TABLE " + org.ogcs.log.mysql.MySQL.tableName(database, oldTableName) + " TO " + org.ogcs.log.mysql.MySQL.tableName(database, newTableName);
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
