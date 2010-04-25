/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.jahath.tests;

import java.net.Socket;

import org.junit.Test;

import com.google.code.jahath.common.connection.SocketConnection;
import com.google.code.jahath.testutils.EchoTestUtil;

public class EchoTest {
    @Test
    public void test() throws Exception {
        OSGiRuntime vchServer = new OSGiRuntime();
        try {
            vchServer.cmd("vchsvc add vch echo");
            vchServer.cmd("port add 9001 vch");
            OSGiRuntime vchClient = new OSGiRuntime();
            try {
                vchClient.cmd("direct-http add http direct");
                vchClient.cmd("vchep add remote-echo localhost:9001 echo http");
                vchClient.cmd("forward add local-echo remote-echo");
                vchClient.cmd("port add 9000 local-echo");
                Thread.sleep(1500); // TODO
                Socket socket = new Socket("localhost", 9000);
                EchoTestUtil.testEcho(new SocketConnection(socket));
                socket.close();
            } finally {
                vchClient.stop();
            }
        } finally {
            vchServer.stop();
        }
    }
}
