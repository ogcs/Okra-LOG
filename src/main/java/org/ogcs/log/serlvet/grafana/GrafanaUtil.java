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

package org.ogcs.log.serlvet.grafana;

import org.ogcs.log.serlvet.ApiHandler;

/**
 * Grafana Json DataSource HTTP API interface.
 * <p>
 * https://grafana.net/plugins/grafana-simple-json-datasource
 *
 * @author TinyZ
 * @date 2016-10-12.
 */
public final class GrafanaUtil {

    private GrafanaUtil() {
        // no-op
    }

    private static final String GRAFANA = "/grafana";

    public static void register() {
        ApiHandler.register(GRAFANA + "/", new GfnTestServlet());
        ApiHandler.register(GRAFANA + "/search", new GfnSearchServlet());
        ApiHandler.register(GRAFANA + "/query", new GfnQueryServlet());
        ApiHandler.register(GRAFANA + "/annotations", new GfnTestServlet());
    }
}
