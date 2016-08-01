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

package org.ogcs.log.core.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import org.ogcs.log.core.MissionBoard;
import org.ogcs.log.core.handler.IpMatcher;
import org.ogcs.log.core.handler.UdpProtocolHandler;
import org.ogcs.log.core.handler.LogRecordHandler;
import org.ogcs.netty.impl.UdpProtocol;

/**
 * Okra-LOG server.
 *
 * @author TinyZ.
 * @date 2016-07-05.
 */
public class UdpLogServer extends UdpProtocol {

    private MissionBoard board;
    private IpMatcher ipMatcher;

    public UdpLogServer(int port, MissionBoard board) {
        this(port, board, null);
    }

    public UdpLogServer(int port, MissionBoard board, IpMatcher filter) {
        super(port);
        this.board = board;
        this.ipMatcher = filter;
    }

    @Override
    protected ChannelHandler newChannelInitializer() {
        return new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(DatagramChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast("ipMatcher", new UdpProtocolHandler(ipMatcher));
                cp.addLast("handler", new LogRecordHandler(board));
            }
        };
    }
}
