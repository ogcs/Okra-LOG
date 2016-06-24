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

package org.ogcs.log.disruptor;

import com.lmax.disruptor.EventHandler;
import org.ogcs.log.AoContext;
import org.ogcs.log.mysql.MySQL2;
import org.ogcs.log.mysql.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AoEventHandler implements EventHandler<AoEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AoEventHandler.class);

    @Override
    public void onEvent(AoEvent event, long sequence, boolean endOfBatch) {
        String[] data = event.get();
        if (data != null) {
            try {
                Table table = AoContext.INSTANCE.XML.getTable(data[0]);
                if (table != null) {
                    String query = MySQL2.sqlInsert(table, data);



                }
            } finally {
                event.setValues(null);
            }
        }



        if (event != null) {

//            EosMultiPoolContext context = event.getContext();
//            if (context != null) {
//                Connection connection = null;
//                try {
//                    connection = context.getConnection();
//                    Statement statement = connection.createStatement();
//                    statement.execute(event.getSqlQuery());
//                    statement.close();
//                    connection.close();
//                } catch (SQLException e) {
//                    LOG.error("SQL ERROR : " + event.getSqlQuery());
////                    e.printStackTrace();
//                }
//            }
        }
    }
}
