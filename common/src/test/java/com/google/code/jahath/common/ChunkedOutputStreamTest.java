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

public class ChunkedOutputStreamTest {
    @Test
    public void test() throws Exception {
        Server server = new Server(5555);
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
                out.writeLine("POST / HTTP/1.1");
                out.writeHeader("Host", "localhost");
                out.writeHeader("Connection", "keep-alive");
                out.writeHeader("Transfer-Encoding", "chunked");
                out.writeHeader("Content-Type", "application/octet-stream");
                out.flushHeaders();
                ChunkedOutputStream chunked = new ChunkedOutputStream(out);
                CRC expectedCRC = new CRC();
                Random random = new Random();
                for (int i=0; i<100; i++) {
                    int len = 16 + random.nextInt(4080);
                    byte[] buffer = new byte[len];
                    random.nextBytes(buffer);
                    chunked.write(buffer);
                    expectedCRC.update(buffer);
                }
                chunked.close();
                CRLFInputStream in = new CRLFInputStream(socket.getInputStream());
                while (true) {
                    String line = in.readLine();
                    if (line.length() == 0) {
                        break;
                    }
                }
                Assert.assertEquals(expectedCRC.getValue(), actualCRC.getValue());
            } finally {
                socket.close();
            }
        } finally {
            server.stop();
        }
    }
}
