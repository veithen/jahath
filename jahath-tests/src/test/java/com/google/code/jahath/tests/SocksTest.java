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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import org.junit.Test;

import com.google.code.jahath.common.connection.SocketConnection;
import com.google.code.jahath.testutils.EchoTestUtil;

public class SocksTest {
    @Test
    public void testService() throws Exception {
        OSGiRuntime socksServer = new OSGiRuntime();
        try {
            socksServer.cmd("sockssvc add socks direct");
            socksServer.cmd("port add 9000 socks");
            OSGiRuntime echoServer = new OSGiRuntime();
            try {
                echoServer.cmd("port add 9001 echo");
                Thread.sleep(1000); // TODO
                Socket socket = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9000)));
                socket.connect(new InetSocketAddress("localhost", 9001));
                EchoTestUtil.testEcho(new SocketConnection(socket));
                socket.close();
            } finally {
                echoServer.stop();
            }
        } finally {
            socksServer.stop();
        }
    }

    @Test
    public void testGatewayAndService() throws Exception {
        OSGiRuntime socksGateway = new OSGiRuntime();
        try {
            socksGateway.cmd("endpoint add socks-endpoint localhost 8001 direct");
            socksGateway.cmd("socksgw add socks-gateway socks-endpoint");
            socksGateway.cmd("endpoint add remote-echo localhost 8002 socks-gateway");
            socksGateway.cmd("forward add local-echo remote-echo");
            socksGateway.cmd("port add 8000 local-echo");
            OSGiRuntime socksServer = new OSGiRuntime();
            try {
                socksServer.cmd("sockssvc add socks direct");
                socksServer.cmd("port add 8001 socks");
                OSGiRuntime echoServer = new OSGiRuntime();
                try {
                    echoServer.cmd("port add 8002 echo");
                    Thread.sleep(1000); // TODO
                    Socket socket = new Socket("localhost", 8000);
                    EchoTestUtil.testEcho(new SocketConnection(socket));
                    socket.close();
                } finally {
                    echoServer.stop();
                }
            } finally {
                socksServer.stop();
            }
        } finally {
            socksGateway.stop();
        }
    }
}
