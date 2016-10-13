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

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import org.ogcs.log.serlvet.AbstractApiServlet;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * Grafana测试数据源接口
 *
 * 用于Grafana检验JSON-DataSource是否可用. 收到请求返回200即可.
 *
 * @author TinyZ
 * @date 2016-10-13.
 */
public class GfnTestServlet extends AbstractApiServlet {

    @Override
    public HttpResponse doGet(HttpRequest request) {
        return response(HttpVersion.HTTP_1_1, OK);
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        return doGet(request);
    }
}
