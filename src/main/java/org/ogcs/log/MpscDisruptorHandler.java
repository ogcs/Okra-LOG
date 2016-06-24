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

package org.ogcs.log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.ogcs.utilities.StringUtil;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
public class MpscDisruptorHandler extends SimpleChannelInboundHandler<String>{

    public static final char DEFAULT_SEPARATOR  = '|';

    protected static final AtomicLong mqSize = new AtomicLong();
    protected static final Queue<String[]> messageQueue = new ConcurrentLinkedQueue<>();


    public MpscDisruptorHandler() {
        init();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        String[] split = StringUtil.split(msg, DEFAULT_SEPARATOR);

        if (mqSize.get() > 1000000) {

            // TODO: 队列太长啦
        }

        messageQueue.add(split);
        mqSize.incrementAndGet();



    }

    public void init() {
        new Thread(()->{




            while(true) {
                try {
                    if (messageQueue.isEmpty()) {
                        String[] poll = messageQueue.poll();




                    } else {
                        Thread.sleep(5);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO: 捕获处理异常

                }
            }
        }).start();
    }



}
