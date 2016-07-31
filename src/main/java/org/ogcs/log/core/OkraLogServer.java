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

package org.ogcs.log.core;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import org.ogcs.log.core.handler.IpFilter;
import org.ogcs.log.core.handler.UdpFilterHandler;
import org.ogcs.log.core.handler.LogRecordHandler;
import org.ogcs.netty.impl.UdpProtocol;

/**
 * Okra-LOG server.
 *
 * @author TinyZ.
 * @date 2016-07-05.
 */
public class OkraLogServer extends UdpProtocol {

    private MissionBoard board;
    private IpFilter ipFilter;

    public OkraLogServer(int port, MissionBoard board) {
        this(port, board, null);
    }

    public OkraLogServer(int port, MissionBoard board, IpFilter filter) {
        super(port);
        this.board = board;
        this.ipFilter = filter;
    }

    @Override
    protected ChannelHandler newChannelInitializer() {
        return new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(DatagramChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast("ipFilter", new UdpFilterHandler(ipFilter));
                cp.addLast("handler", new LogRecordHandler(board));
            }
        };
    }
}
