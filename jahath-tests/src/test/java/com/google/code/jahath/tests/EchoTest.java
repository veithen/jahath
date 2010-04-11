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

import com.google.code.jahath.Connection;
import com.google.code.jahath.client.vch.VCHClient;
import com.google.code.jahath.common.vch.VCHConstants;
import com.google.code.jahath.server.vch.VCHServer;
import com.google.code.jahath.testutils.EchoConnectionHandler;
import com.google.code.jahath.testutils.EchoTestUtil;

public class EchoTest {
    @Test
    public void test() throws Exception {
        VCHServer server = new VCHServer(5555);
        server.registerService(VCHConstants.Services.ECHO, new EchoConnectionHandler());
        VCHClient client = new VCHClient("localhost", 5555, null);
        Connection connection = client.createConnection(VCHConstants.Services.ECHO);
        EchoTestUtil.testEcho(connection);
        client.shutdown();
        server.stop();
    }
}
