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

import org.ogcs.log.netty.OkraLogServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;

/**
 * @author TinyZ
 * @date 2016-07-05.
 */
public class LogRevTest {

    public static void main(String[] args) throws IOException {
//        OkraLogServer server = new OkraLogServer(9005, null);
//        server.start();
//        server.udpChannel();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        DatagramSocket socket = new DatagramSocket(0);

        String log = "log_money|2016-06-24|openid|0|105|15|100|1000";
        DatagramPacket dp = new DatagramPacket(log.getBytes(Charset.forName("UTF-8")), 0, log.length(), InetAddress.getByName("127.0.0.1"), 9005);

        for (int i = 0; i < 10; i++) {
            socket.send(dp);
            try {
                Thread.sleep((int)(Math.random() * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    @Before
//    public void bootstrapServer() {
//        OkraLogServer server = new OkraLogServer(9005);
//        server.start();
//        server.udpChannel();
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void sendMsg() throws IOException {
//        DatagramSocket socket = new DatagramSocket(0);
//        String log = "MyLogData!";
//
//        DatagramPacket dp = new DatagramPacket(log.getBytes(Charset.forName("UTF-8")), 0, log.length(), InetAddress.getByName("127.0.0.1"), 9005);
//        socket.send(dp);
//    }

}
