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

package org.ogcs.log.core.handler;

import com.lmax.disruptor.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Disruptor 's ringBuffer event handler.
 */
public class LogRecordTaskHandler implements EventHandler<LogRecordTask> {

    private static final Logger LOG = LogManager.getLogger(LogRecordTaskHandler.class);

    @Override
    public void onEvent(LogRecordTask event, long sequence, boolean endOfBatch) {
        try {
            event.record();
        } catch (Exception e) {
            LOG.error("LogRecordTask Error : ", e);
        } finally {
            event.release();
        }
    }
}
