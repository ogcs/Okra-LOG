package org.ogcs.log.util;

import org.ogcs.log.mysql.Field;
import org.ogcs.log.mysql.Table;
import org.ogcs.utilities.StringUtil;

/**
 * @author TinyZ
 * @date 2016/6/26.
 */
public final class MySQL {

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

    private MySQL() {
        //no-op
    }

}
