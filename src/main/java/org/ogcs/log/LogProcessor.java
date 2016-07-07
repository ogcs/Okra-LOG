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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
public class LogProcessor {

    private AtomicLong logsSize = new AtomicLong(0);
    private Struct struct;
    protected Queue<String[]> logs;
    private int limit;

    public LogProcessor(Struct struct) {
        this.struct = struct;
        this.logs = new ConcurrentLinkedQueue<>();
    }


}
