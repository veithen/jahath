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
package com.google.code.jahath.tests;

import org.junit.Test;

import com.google.code.jahath.client.JahathClient;
import com.google.code.jahath.common.connection.Connection;
import com.google.code.jahath.server.JahathServer;
import com.google.code.jahath.testutils.EchoConnectionHandler;
import com.google.code.jahath.testutils.EchoTestUtil;

public class EchoTest {
    @Test
    public void test() throws Exception {
        JahathServer server = new JahathServer(5555, new EchoConnectionHandler());
        JahathClient client = new JahathClient("localhost", 5555, null);
        Connection connection = client.createConnection();
        EchoTestUtil.testEcho(connection);
        client.shutdown();
        server.stop();
    }
}
