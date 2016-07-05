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
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.AoContext;
import org.ogcs.log.LogProcessor;
import org.ogcs.log.parser.Table;
import org.ogcs.utilities.StringUtil;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
@Sharable
public class LogRecordHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger LOG = LogManager.getLogger(LogRecordHandler.class);

    public static final char DEFAULT_SEPARATOR = '|';

    protected static final Queue<String[]> messageQueue = new ConcurrentLinkedQueue<>();

    protected Map<String/*Table.name*/, Queue<String[]>> queue;


    protected Map<String/*Table.name*/, LogProcessor> queue2;

    private LogProcessor processor;
    private char separator;

    public LogRecordHandler() {

    }

    public LogRecordHandler(LogProcessor processor) {
        this.separator = DEFAULT_SEPARATOR;
        this.processor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket dp) throws Exception {
        String msg = dp.content().toString(Charset.forName("UTF-8"));

        String[] split = StringUtil.split(msg, separator);

        Table table = AoContext.INSTANCE.XML.getTable(split[0]);
        if (table == null) {
            LOG.error("Unknown table [ " + split[0] + " ], msg : " + msg);
            return;
        }
        Queue<String[]> msgQueue = queue.get(split[0]);
        if (msgQueue == null) {
            msgQueue = new ConcurrentLinkedQueue<>();
            queue.put(split[0], msgQueue);
        }
        msgQueue.add(split); // 重新拷贝一份


        messageQueue.add(split);
    }


}
