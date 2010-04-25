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

import org.junit.Test;

import com.google.code.jahath.Connection;
import com.google.code.jahath.client.vch.VCHClient;
import com.google.code.jahath.common.vch.VCHConstants;
import com.google.code.jahath.service.echo.EchoService;
import com.google.code.jahath.service.vch.VCHServer;
import com.google.code.jahath.testutils.EchoTestUtil;

public class EchoTest {
    @Test
    public void test() throws Exception {
        OSGiRuntime vchServer = new OSGiRuntime();
        try {
            vchServer.cmd("vchsvc add vch echo");
            vchServer.cmd("port add 9001 vchsvc");
            OSGiRuntime vchClient = new OSGiRuntime();
            try {
                vchClient.cmd("direct-http add http direct");
                vchClient.cmd("");
            } finally {
                vchClient.stop();
            }
        } finally {
            vchServer.stop();
        }
        
        
        VCHServer server = new VCHServer(5555);
        server.registerService(VCHConstants.Services.ECHO, new EchoService());
        VCHClient client = new VCHClient("localhost", 5555, null);
        Connection connection = client.createConnection(VCHConstants.Services.ECHO);
        EchoTestUtil.testEcho(connection);
        client.shutdown();
        server.stop();
    }
}
