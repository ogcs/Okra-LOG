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
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.config.OkraProperties;
import org.ogcs.utilities.StringUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Filter message by host(ip) with UDP protocol.
 *
 * @author TinyZ.
 * @date 2016-06-24.
 */
@Sharable
public final class UdpProtocolHandler
        extends MessageToMessageDecoder<DatagramPacket>
        implements Filter<DatagramPacket>, Translator<DatagramPacket, String> {

    private static final Logger LOG = LogManager.getLogger(UdpProtocolHandler.class);

    private IpMatcher matcher;

    public UdpProtocolHandler(IpMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        if (filter(msg)) {
            InetSocketAddress sender = msg.sender();
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
    public boolean filter(DatagramPacket msg) {
        return matcher != null && !matcher.accept(msg.sender().getHostName());
    }

    @Override
    public String translate(DatagramPacket msg) {
        ByteBuf content = msg.content();
        if (content.readableBytes() <= 0) {
            return null;
        }
        return content.toString(Charset.forName("UTF-8"));
    }
}
