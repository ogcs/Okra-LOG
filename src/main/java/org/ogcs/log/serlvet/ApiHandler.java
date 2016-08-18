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

package org.ogcs.log.serlvet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author TinyZ
 */
@Sharable
public class ApiHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger LOG = LogManager.getLogger(ApiHandler.class);

    private static final Map<String /*api path*/, ApiServlet> SERVLETS = new HashMap<>();

    public ApiHandler() {
    }

    public static void register(ApiServlet servlet) {
        SERVLETS.put(servlet.path(), servlet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        QueryStringDecoder decoder = new QueryStringDecoder(msg.uri());
        switch (decoder.path()) {
            case "/favicon.ico":
                response(ctx, NOT_FOUND);
                break;
            default: {
                ApiServlet servlet = SERVLETS.get(decoder.path());
                if (servlet == null) {
                    response(ctx, FORBIDDEN);
                    return;
                }
                HttpResponse response;
                if (msg.method() == HttpMethod.POST) {
                    response = servlet.doPost(msg);
                } else {
                    response = servlet.doGet(msg);
                }
                if (response == null) {
                    response(ctx, BAD_REQUEST);
                    return;
                }
                response(ctx, response);
                break;
            }
        }
    }

    private void response(ChannelHandlerContext ctx, HttpResponseStatus status) {
        ChannelFuture future = ctx.writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, status));
        future.addListener(ChannelFutureListener.CLOSE);
    }

    private void response(ChannelHandlerContext ctx, HttpResponse response) {
        ChannelFuture future = ctx.writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    private void response(ChannelHandlerContext ctx, String msg) {
        HttpResponse response = null;
        if (msg != null) {
            ByteBuf byteBuf = Unpooled.wrappedBuffer(msg.getBytes());
            response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        } else {
            response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        }
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
}
