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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

/**
 * HTTP protocol filter.
 *
 * @author TinyZ.
 * @since 1.0
 */
@Sharable
public final class HttpFilter
        extends MessageToMessageDecoder<FullHttpRequest>
        implements Filter<ChannelHandlerContext>, Translator<FullHttpRequest, String> {

    private static final Logger LOG = LogManager.getLogger(HttpFilter.class);

    private IpMatcher filter;

    public HttpFilter(IpMatcher filter) {
        this.filter = filter;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (filter(ctx)) {
            InetSocketAddress sender = (InetSocketAddress)ctx.channel().remoteAddress();
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
        return filter != null && !filter.accept(address.getHostName());
    }

    @Override
    public String translate(FullHttpRequest msg) {
        ByteBuf content = msg.content();
        if (content.readableBytes() <= 0) {
            return null;
        }
        return content.toString(Charset.forName("UTF-8"));
    }
}
