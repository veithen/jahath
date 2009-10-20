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
package com.google.code.jahath.common;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import com.google.code.jahath.common.http.ChunkedInputStream;
import com.google.code.jahath.common.http.HttpOutputStream;

public class ChunkedInputStreamTest {
    @Test
    public void test() throws Exception {
        Server server = new Server(5555);
        final CRC expectedCRC = new CRC();
        server.setHandler(new AbstractHandler() {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response,
                    int dispatch) throws IOException, ServletException {
                Random random = new Random();
                response.setContentType("application/octet-stream");
                OutputStream out = response.getOutputStream();
                for (int i=0; i<100; i++) {
                    int len = 16 + random.nextInt(4080);
                    byte[] buffer = new byte[len];
                    random.nextBytes(buffer);
                    out.write(buffer);
                    out.flush();
                    expectedCRC.update(buffer);
                }
                ((Request)request).setHandled(true);
            }
        });
        server.start();
        try {
            Socket socket = new Socket("localhost", 5555);
            try {
                HttpOutputStream out = new HttpOutputStream(socket.getOutputStream());
                out.writeLine("GET / HTTP/1.1");
                out.writeHeader("Host", "localhost");
                out.writeHeader("Connection", "keep-alive");
                out.flushHeaders();
                CRLFInputStream in = new CRLFInputStream(socket.getInputStream());
                while (true) {
                    String line = in.readLine();
                    if (line.length() == 0) {
                        break;
                    }
                }
                ChunkedInputStream chunked = new ChunkedInputStream(in);
                CRC actualCRC = new CRC();
                actualCRC.update(chunked);
                Assert.assertEquals(expectedCRC.getValue(), actualCRC.getValue());
            } finally {
                socket.close();
            }
        } finally {
            server.stop();
        }
    }
}
