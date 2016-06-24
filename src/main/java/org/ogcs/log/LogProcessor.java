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

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.ogcs.log.disruptor.AoEvent;
import org.ogcs.log.disruptor.AoEventHandler;

import java.util.Queue;
import java.util.concurrent.Executors;

/**
 * @author TinyZ
 * @date 2016/6/24.
 */
public class LogProcessor implements Runnable {

    private static final int ringBufferSize = 1024 * 128;
    protected Disruptor<AoEvent> disruptor;
    protected Queue<String[]> messageQueue;

    public LogProcessor(Queue<String[]> messageQueue) {
        this(messageQueue, new Disruptor<>(
                AoEvent.DEFAULT_EVENT_FACTORY, ringBufferSize, Executors.newCachedThreadPool(), ProducerType.SINGLE, new BlockingWaitStrategy()
        ), new AoEventHandler(), null);
    }

    public LogProcessor(Queue<String[]> messageQueue, Disruptor<AoEvent> disruptor, EventHandler<AoEvent> handler, ExceptionHandler<AoEvent> exceptionHandler) {
        if (messageQueue == null) {
            throw new NullPointerException("messageQueue");
        }
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.messageQueue = messageQueue;
        this.disruptor = disruptor;
        this.disruptor.handleEventsWith(handler);
        this.disruptor.handleExceptionsWith(exceptionHandler);
        this.disruptor.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (messageQueue.isEmpty()) {
                    String[] poll = messageQueue.poll();
                    if (poll != null) {
                        RingBuffer<AoEvent> ringBuffer = disruptor.getRingBuffer();
                        long next = ringBuffer.next();
                        try {
                            AoEvent aoEvent = ringBuffer.get(next);
                            aoEvent.setValues(poll);
                        } finally {
                            ringBuffer.publish(next);
                        }
                    }
                } else {
                    Thread.sleep(5);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                // TODO: 处理中断
            } catch (Exception e) {
                // TODO: 捕获处理异常

            }
        }
    }
}
