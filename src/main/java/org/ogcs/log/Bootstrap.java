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
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.ogcs.log.disruptor.AoEvent;
import org.ogcs.log.disruptor.AoEventHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author TinyZ
 * @date 2016-07-03.
 */
public class Bootstrap {

    public static void main(String[] args) {


        Disruptor<AoEvent> disruptor = new Disruptor<AoEvent>(
                new EventFactory<AoEvent>() {
                    @Override
                    public AoEvent newInstance() {
                        return new AoEvent();
                    }
                }
        , 1024, Executors.newFixedThreadPool(20), ProducerType.MULTI, new BlockingWaitStrategy());
//        disruptor.handleEventsWith(new AoEventHandler());
        disruptor.handleEventsWithWorkerPool(new WorkHandler<AoEvent>() {
            @Override
            public void onEvent(AoEvent event) throws Exception {

            }
        });



    }
}
