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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple IP matcher. Use to filter report log info.
 *
 * @author TinyZ
 * @since 1.0
 */
public class IpMatcher {

    private static final boolean DEFAULT_ACCEPT = true;
    /**
     * Default value. if the filter is empty.
     */
    private boolean nuAccept = false;

    private Map<String /* host */, Boolean /* accept */> filter;

    public IpMatcher() {
        this(new ConcurrentHashMap<>(), DEFAULT_ACCEPT);
    }

    public IpMatcher(Map<String, Boolean> filter, boolean nuAccept) {
        this.filter = filter;
        this.nuAccept = nuAccept;
    }

    public boolean accept(String host) {
        Boolean accept = filter.get(host);
        return accept == null ? nuAccept : accept;
    }

    public void add(String host, boolean accept) {
        filter.put(host, accept);
    }

    public void remove(String host) {
        filter.remove(host);
    }
}
