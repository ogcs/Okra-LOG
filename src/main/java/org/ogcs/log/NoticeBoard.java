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

import org.ogcs.log.parser.Struct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TinyZ
 * @date 2016-07-06.
 */
public class NoticeBoard {

    private Map<String, Struct> board;

    public NoticeBoard() {
        this.board = new ConcurrentHashMap<>();
    }

    public void add(String tableName, String[] params) {
        Struct processor = board.get(tableName);
        if (processor == null) {
            processor = new Struct();
            board.put(tableName, processor);
        }
        processor.add(params);
    }

}
