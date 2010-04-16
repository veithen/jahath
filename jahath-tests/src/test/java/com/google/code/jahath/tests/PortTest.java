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

import com.google.code.jahath.Connection;
import com.google.code.jahath.common.connection.SocketConnection;
import com.google.code.jahath.testutils.EchoTestUtil;

public class PortTest {
    @Test
    public void test() throws Exception {
        OSGiRuntime osgi = new OSGiRuntime();
        try {
            osgi.cmd("port add 5555 echo");
            Thread.sleep(1000); // TODO
            Connection connection = new SocketConnection(new Socket("localhost", 5555));
            try {
                EchoTestUtil.testEcho(connection);
            } finally {
                connection.close();
            }
        } finally {
            osgi.stop();
        }
    }
}
