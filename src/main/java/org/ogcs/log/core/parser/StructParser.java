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

package org.ogcs.log.core.parser;

import org.ogcs.log.core.builder.Table;

import java.util.Map;

/**
 * Database table's struct mapper parser.
 *
 * @author TinyZ
 * @date 2016-06-24
 * @since 1.0
 */
public interface StructParser<T> {

    /**
     * Struct table lab. The table defined mysql table attribute and field data.
     */
    String STRUCT_TABLE = "table";
    /**
     * Struct field lab, The field element is table's child element. use to defined field attribute.
     */
    String STRUCT_FIELD = "field";
    /**
     * table element 's child element. Use to defined database's index list.
     */
    String STRUCT_INDEXES = "indexes";
    /**
     * indexes element 's child element. Use to defined database's index.
     */
    String STRUCT_INDEX = "index";
    /**
     * Get {@link Table}  form tables pool by unique tableName.
     * <p>The table name must be lowercase</p>
     *
     * @param name unique tableName.
     * @return Return The table is it exist, if tables pool size is zero, will load XML config file to initialize table pool.
     */
    T getTable(String name);

    /**
     * Get all {@link Table}
     *
     * @return Return all registered T
     */
    Map<String, T> getAll();

    /**
     * If the struct defined is load and tables data is initialized
     *
     * @return Return true if the tables data is initialized, false otherwise.
     */
    boolean isInitialized();

    /**
     * Load struct defined file
     */
    void load();

    /**
     * Reload struct defined file and return the tables
     *
     * @param filePath The struct defined file path
     * @return Return the table map
     */
    Map<String, T> load(String filePath);

    /**
     * Reload struct defined file
     */
    void reload();

    /**
     * Reload struct defined file , replace tables data and  return the tables
     *
     * @param filePath xml file path
     * @return Return the table map
     */
    Map<String, T> loadAndReplace(String filePath);

    /**
     * After execute load method and verify the table map . Use this method to replace the table data
     *
     * @param tables The table map
     */
    void replace(Map<String, T> tables);

    /**
     * After execute load method and verify the table map . Use this method to replace the table data and filepath
     *
     * @param tables   The table map
     * @param filePath The eys of sauron log system struct file path
     */
    void replace(Map<String, T> tables, String filePath);
}
