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
package com.google.code.jahath.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.CRLFOutputStream;
import com.google.code.jahath.common.ChunkedInputStream;
import com.google.code.jahath.common.ChunkedOutputStream;
import com.google.code.jahath.common.Headers;

class ConnectionHandler implements Runnable {
    private static final Log log = LogFactory.getLog(ConnectionHandler.class);
    
    private final Socket socket;
    private final JahathServer2 server;

    public ConnectionHandler(Socket socket, JahathServer2 server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            CRLFInputStream request = new CRLFInputStream(socket.getInputStream());
            CRLFOutputStream response = new CRLFOutputStream(socket.getOutputStream());
            String requestLine = request.readLine();
            Headers headers = new Headers(request);
            // TODO: do this properly!
            String[] parts = requestLine.split(" ");
            String path = parts[1];
            int type;
            SessionWrapper session;
            if (path.equals("/")) {
                type = 1;
                session = server.createSession();
            } else {
                String sessionId = path.substring(1);
                session = server.getSession(sessionId);
                type = headers.getHeader("Content-Type") != null ? 2 : 3;
            }
            if (type == 2) {
                IOUtils.copy(new ChunkedInputStream(request), session.getSession().getOutputStream());
            }
            response.writeLine("HTTP/1.1 200 OK");
            if (type == 1) {
                response.writeLine("X-JHT-Session-Id: " + session.getId());
            } else if (type == 3) {
                response.writeLine("Content-Type: application/octet-stream");
                response.writeLine("Transfer-Encoding: chunked");
            }
            response.writeLine("Connection: keep-alive");
            response.writeLine("");
            if (type == 3) {
                InputStream in = session.getSession().getInputStream();
                OutputStream out = new ChunkedOutputStream(response);
                // TODO: loop here unless available() return 0
                byte[] buffer = new byte[4096];
                int c = in.read(buffer);
                out.write(buffer, 0, c);
                out.close();
            }
            response.flush();
            socket.close();
        } catch (Exception ex) {
            log.error("", ex);
        }
    }
}
