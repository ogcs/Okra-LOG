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

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * HTTP protocol matcher.
 *
 * @author TinyZ.
 * @since 1.0
 */
@Sharable
public final class HttpProtocolHandler
        extends MessageToMessageDecoder<FullHttpRequest>
        implements Filter<ChannelHandlerContext>, Translator<FullHttpRequest, String> {

    private static final Logger LOG = LogManager.getLogger(HttpProtocolHandler.class);

    private IpMatcher matcher;

    public HttpProtocolHandler(IpMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (filter(ctx)) {
            InetSocketAddress sender = (InetSocketAddress) ctx.channel().remoteAddress();
            LOG.info("Access denied for host(IP) : [" + sender.getHostName() + ":" + sender.getPort() + "].");
            return;
        }
        String data = translate(msg);
        if (data == null) {
            LOG.info("The message data is null.");
            return;
        }
        out.add(data);
    }

    @Override
    public boolean filter(ChannelHandlerContext ctx) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        return matcher != null && !matcher.accept(address.getHostName());
    }

    @Override
    public String translate(FullHttpRequest msg) {
        String data = msg.uri();
        int index = data.indexOf("?");
        if (index < 0) {
            return null;
        }
        String params = data.substring(index + 1, data.length());
        if (params.length() <= 0) {
            return null;
        }
        return data;
    }
}
