/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.jahath.server.socks;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import org.junit.Test;

import com.google.code.jahath.common.connection.SocketConnection;
import com.google.code.jahath.common.server.Server;
import com.google.code.jahath.testutils.EchoConnectionHandler;
import com.google.code.jahath.testutils.EchoTestUtil;

public class SocksTest {
    @Test
    public void test() throws Exception {
        Server socksServer = new Server(9000, new SocksConnectionHandler());
        Server echoServer = new Server(9001, new EchoConnectionHandler());
        Socket socket = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9000)));
        socket.connect(new InetSocketAddress("localhost", 9001));
        EchoTestUtil.testEcho(new SocketConnection(socket));
        socket.close();
        echoServer.stop();
        socksServer.stop();
    }
}
