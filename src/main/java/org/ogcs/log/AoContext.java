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

import org.ogcs.log.parser.StructParser;
import org.ogcs.log.parser.Table;
import org.ogcs.log.parser.W3cDomParser;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
public enum AoContext {

    INSTANCE;

    public final StructParser<Table> XML = new W3cDomParser("");


}
