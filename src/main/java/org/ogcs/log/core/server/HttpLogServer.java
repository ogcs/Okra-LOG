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
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.ogcs.log.core.MissionBoard;
import org.ogcs.log.core.handler.HttpProtocolHandler;
import org.ogcs.log.core.handler.IpMatcher;
import org.ogcs.log.core.handler.LogRecordHandler;
import org.ogcs.netty.impl.TcpProtocolServer;

/**
 * HTTP protocol.
 *
 * @author TinyZ.
 * @since 1.0
 */
public class HttpLogServer extends TcpProtocolServer {

    private MissionBoard board;
    private IpMatcher ipMatcher;

    public HttpLogServer(int port, MissionBoard board) {
        this(port, board, null);
    }

    public HttpLogServer(int port, MissionBoard board, IpMatcher filter) {
        setPort(port);
        this.board = board;
        this.ipMatcher = filter;
    }

    @Override
    protected ChannelHandler newChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                cp.addLast("codec", new HttpServerCodec());
                cp.addLast("aggregator", new HttpObjectAggregator(1048576));
                cp.addLast("ipMatcher", new HttpProtocolHandler(ipMatcher));
                cp.addLast("handler", new LogRecordHandler(board));
            }
        };
    }
}
