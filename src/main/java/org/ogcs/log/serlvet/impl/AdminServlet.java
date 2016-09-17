package org.ogcs.log.serlvet.impl;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.ogcs.log.serlvet.AbstractApiServlet;

/**
 * @author TinyZ
 * @date 2016-08-02.
 */
public class AdminServlet extends AbstractApiServlet {

    @Override
    public HttpResponse doGet(HttpRequest request) {
        System.out.println(request.uri());
        return response(0);
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        return null;
    }
}
