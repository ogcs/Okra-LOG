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

package org.ogcs.log.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
@Sharable
public class IpFilterHandler extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger LOG = LogManager.getLogger(IpFilterHandler.class);

    private IpFilter filter;

    public IpFilterHandler(IpFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        InetSocketAddress sender = msg.sender();
        String host = sender.getHostName();
        if (filter != null && !filter.accept(host)) {
            LOG.info("Access denied for IP : [" + host + ":" + sender.getPort() + "]");
            return;
        }
        out.add(msg.content().toString(Charset.forName("UTF-8")));
    }
}
