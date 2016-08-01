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

/**
 * Translate reported message to java bean.
 *
 * @author TinyZ.
 * @since 1.0
 */
public interface Translator<M, O> {

    /**
     * Translate client reported message to object.
     *
     * @param msg the client reported message.
     * @return Return the message data if exist.
     */
    O translate(M msg);
}
