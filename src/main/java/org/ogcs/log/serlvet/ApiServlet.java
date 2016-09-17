package org.ogcs.log.serlvet;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * Web Servlet.
 *
 * @author TinyZ
 * @date 2016-08-01.
 */
public interface ApiServlet {

    HttpResponse doGet(HttpRequest request);

    HttpResponse doPost(HttpRequest request);

}
