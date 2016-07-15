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

/**
 * @author TinyZ
 * @date 2016-07-14.
 */
public class VolatileTest {

    private volatile boolean isWriting = false;

    public static void main(String[] args) {
        VolatileTest test = new VolatileTest();
        test.test();
    }

    //    @Test
    public void test() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                write1("write1 Writing.", "write1 Done");
                try {
//            Thread.sleep((int) (Math.random() * 10L));
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                write1("write2 Writing.", "write2 Done");
                try {
//            Thread.sleep((int) (Math.random() * 10L));
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void write1(String msg1, String msg2) {
        if (isWriting) {
            System.out.println(msg1);
            return;
        }
        isWriting = true;
        try {
//            Thread.sleep((int) (Math.random() * 10L));
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(msg2);
        isWriting = false;
    }

}
