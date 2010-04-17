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
package com.google.code.jahath.common.http;

import java.io.IOException;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.testutils.CRC;

public class ChunkedInputStreamTest {
    private void test(int requestCount) throws Exception {
        Server server = new Server(5555);
        final CRC expectedCRC = new CRC();
        server.setHandler(new AbstractHandler() {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response,
                    int dispatch) throws IOException, ServletException {
                response.setContentType("application/octet-stream");
                Util.writeRandomData(response.getOutputStream(), expectedCRC);
                ((Request)request).setHandled(true);
            }
        });
        server.start();
        try {
            Socket socket = new Socket("localhost", 5555);
            try {
                HttpOutputStream out = new HttpOutputStream(socket.getOutputStream());
                CRLFInputStream in = new CRLFInputStream(socket.getInputStream());
                for (int i=0; i<requestCount; i++) {
                    out.writeLine("GET / HTTP/1.1");
                    out.writeHeader("Host", "localhost");
                    out.writeHeader("Connection", "keep-alive");
                    out.flushHeaders();
                    Util.consumeHeaders(in);
                    ChunkedInputStream chunked = new ChunkedInputStream(in);
                    CRC actualCRC = new CRC();
                    actualCRC.update(chunked);
                    Assert.assertEquals(expectedCRC.getValue(), actualCRC.getValue());
                    expectedCRC.reset();
                    actualCRC.reset();
                }
            } finally {
                socket.close();
            }
        } finally {
            server.stop();
        }
    }

    @Test
    public void testWithoutKeepAlive() throws Exception {
        test(1);
    }

    @Test
    public void testWithKeepAlive() throws Exception {
        test(4);
    }
}
