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

package org.ogcs.log.xml;

import java.util.Map;

/**
 * XML interpreter.
 *
 * @author TinyZ
 * @date 2016-06-24
 * @since 1.0
 */
public interface XmlInterpreter<Table> {

    /**
     * XML element table. The table defined mysql table attribute and field data.
     */
    String XML_ELEMENT_TABLE = "table";

    /**
     * XML element field, The field element is table's child element. use to defined field attribute.
     */
    String XML_ELEMENT_FIELD = "field";

    /**
     * Get {@link org.ogcs.log.mysql.Table}  form tables pool by unique tableName.
     * <p>The table name must be lowercase</p>
     *
     * @param name unique tableName.
     * @return Return The table is it exist, if tables pool size is zero, will load XML config file to initialize table pool.
     */
    Table getTable(String name);

    /**
     * Get all {@link org.ogcs.log.mysql.Table}
     *
     * @return Return all registered Table
     */
    Map<String, Table> getAllTables();

    /**
     * Reload xml file
     */
    void reload();

    /**
     * Reload xml file and return the tables
     *
     * @param filePath xml file path
     * @return Return the table map
     */
    Map<String, Table> loadXML(String filePath);

    /**
     * Reload xml file , replace tables data and  return the tables
     *
     * @param filePath xml file path
     * @return Return the table map
     */
    Map<String, Table> loadXmlAndReplace(String filePath);

    /**
     * Load xml file
     */
    void load();

    /**
     * If the xml is load and tables data is initialized
     *
     * @return Return true if the tables data is initialized, false otherwise.
     */
    boolean isInitialized();

    /**
     * After execute loadXML method and verify the table map . Use this method to replace the table data
     *
     * @param tables The table map
     */
    void replace(Map<String, Table> tables);

    /**
     * After execute loadXML method and verify the table map . Use this method to replace the table data and filepath
     *
     * @param tables   The table map
     * @param filePath The eys of sauron log system struct file path
     */
    void replace(Map<String, Table> tables, String filePath);
}
