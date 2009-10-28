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
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.connection.Connection;
import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.server.Server;
import com.google.code.jahath.common.testutils.CRC;

public class ChunkedOutputStreamTest {
    private void testClient(int requestCount) throws Exception {
        org.mortbay.jetty.Server server = new org.mortbay.jetty.Server(5555);
        final CRC actualCRC = new CRC();
        server.setHandler(new AbstractHandler() {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response,
                    int dispatch) throws IOException, ServletException {
                actualCRC.update(request.getInputStream());
                ((Request)request).setHandled(true);
            }
        });
        server.start();
        try {
            Socket socket = new Socket("localhost", 5555);
            try {
                HttpOutputStream out = new HttpOutputStream(socket.getOutputStream());
                CRLFInputStream in = new CRLFInputStream(socket.getInputStream());
                CRC expectedCRC = new CRC();
                for (int i=0; i<requestCount; i++) {
                    out.writeLine("POST / HTTP/1.1");
                    out.writeHeader("Host", "localhost");
                    out.writeHeader("Connection", "keep-alive");
                    out.writeHeader("Transfer-Encoding", "chunked");
                    out.writeHeader("Content-Type", "application/octet-stream");
                    out.flushHeaders();
                    ChunkedOutputStream chunked = new ChunkedOutputStream(out);
                    Util.writeRandomData(chunked, expectedCRC);
                    chunked.close();
                    Util.consumeHeaders(in);
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
    public void testClientWithoutKeepAlive() throws Exception {
        testClient(1);
    }

    @Test
    public void testClientWithKeepAlive() throws Exception {
        testClient(4);
    }
    
    public void testServer(int requestCount) throws Exception {
        final CRC expectedCRC = new CRC();
        Server server = new Server(5555, new ConnectionHandler() {
            public void handle(ExecutionEnvironment env, Connection connection) {
                try {
                    CRLFInputStream in = new CRLFInputStream(connection.getInputStream());
                    HttpOutputStream out = new HttpOutputStream(connection.getOutputStream());
                    while (true) {
                        Util.consumeHeaders(in);
                        out.writeLine("HTTP/1.1 200 OK");
                        out.writeHeader("Connection", "keep-alive");
                        out.writeHeader("Transfer-Encoding", "chunked");
                        out.writeHeader("Content-Type", "application/octet-stream");
                        out.flushHeaders();
                        ChunkedOutputStream chunked = new ChunkedOutputStream(out);
                        Util.writeRandomData(chunked, expectedCRC);
                        chunked.close();
                    }
                } catch (IOException ex) {
                    // TODO
                    ex.printStackTrace();
                }
            }
        });
        try {
            CRC actualCRC = new CRC();
            for (int i=0; i<requestCount; i++) {
                InputStream in = new URL("http://localhost:5555/").openStream();
                actualCRC.update(in);
                in.close();
                Assert.assertEquals(expectedCRC.getValue(), actualCRC.getValue());
                expectedCRC.reset();
                actualCRC.reset();
            }
        } finally {
            server.stop();
        }
    }

    @Test
    public void testServerWithoutKeepAlive() throws Exception {
        testServer(1);
    }

    @Test
    public void testServerWithKeepAlive() throws Exception {
        testServer(4);
    }
}
