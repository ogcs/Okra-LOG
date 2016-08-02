package org.ogcs.log.serlvet.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import org.ogcs.log.serlvet.ApiServlet;

/**
 * @author TinyZ
 * @date 2016-08-02.
 */
public class AdminServlet implements ApiServlet {

    @Override
    public String path() {
        return "/api.action";
    }

    @Override
    public HttpResponse doGet(HttpRequest request) {
        System.out.println(request.uri());
        ByteBuf byteBuf = Unpooled.copiedBuffer("{state:0}".getBytes());
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
    }

    @Override
    public HttpResponse doPost(HttpRequest request) {
        return null;
    }
}
