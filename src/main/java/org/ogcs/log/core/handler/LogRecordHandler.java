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
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ogcs.log.core.MissionBoard;
import org.ogcs.log.core.builder.Table;
import org.ogcs.utilities.StringUtil;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
@Sharable
public class LogRecordHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOG = LogManager.getLogger(LogRecordHandler.class);

    private final MissionBoard missions;

    public LogRecordHandler(MissionBoard missions) {
        this.missions = missions;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (LOG.isInfoEnabled())
            LOG.info("Report Log : " + msg);
        if (missions == null) {
            return;
        }
        String[] split = StringUtil.split(msg, missions.getConfig().getLogSeparator());
        Table table = missions.getParser().getTable(split[0]);
        if (table == null) {
            LOG.error("Unknown table [ " + split[0] + " ], msg : " + msg);
            return;
        }
        if (table.getFields().length + 1 != split.length) {
            LOG.error("[msg] log param element size is [" + split.length + "]. Field size is [ " + split.length + " ], msg : " + msg);
            return;
        }
        missions.add(split[0], split);
    }
}
