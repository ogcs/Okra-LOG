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

import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import org.ogcs.netty.impl.UdpProtocol;

import java.nio.charset.Charset;

/**
 * @author TinyZ
 * @date 2016-07-05.
 */
public class OkraLogServer extends UdpProtocol {

    private IpFilter ipFilter;

    public OkraLogServer(int port) {
        super(port);
    }

    public OkraLogServer(int port, IpFilter ipFilter) {
        super(port);
        this.ipFilter = ipFilter;
    }

    @Override
    protected ChannelHandler newChannelInitializer() {
        return new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(DatagramChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast("ipFilter", new IpFilterHandler(ipFilter));
                cp.addLast("handler", new MpscDisruptorHandler());

                cp.addLast("handler", new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket dp) throws Exception {
                        String msg = dp.content().toString(Charset.forName("UTF-8"));
                        System.out.println("Received : " + msg);

                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        super.exceptionCaught(ctx, cause);
                    }
                });
            }
        };
    }
}
