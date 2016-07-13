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

package org.ogcs.log;

import org.ogcs.log.core.builder.Field;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.util.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 从数据库读取Table信息并创建Table的Bean
 *
 * @author TinyZ
 * @date 2016-07-04.
 */

public class CreateTableTest1 {

    @org.junit.Test
    public void createTable() {
        try {
            Connection root = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/okra_log_table?&useUnicode=true&characterEncoding=UTF8&useSSL=false", "wooduan", "123456"); // jdbc:mysql://localhost/database

            Table<Field> table = MySQL.newTable(root, null, "test_table");

            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
