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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.google.code.jahath.server.http.HttpRequest;
import com.google.code.jahath.server.http.HttpRequestHandler;
import com.google.code.jahath.server.http.HttpResponse;

public class HttpRequestHandlerImpl implements HttpRequestHandler {
    private final JahathServer2 server;

    public HttpRequestHandlerImpl(JahathServer2 server) {
        this.server = server;
    }

    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        int type;
        SessionWrapper session;
        if (path.equals("/")) {
            type = 1;
            session = server.createSession();
        } else {
            String sessionId = path.substring(1);
            session = server.getSession(sessionId);
            type = request.getHeader("Content-Type") != null ? 2 : 3;
        }
        if (type == 2) {
            IOUtils.copy(request.getInputStream(), session.getSession().getOutputStream());
        }
        response.setStatus(200);
        response.addHeader("Connection", "keep-alive"); // TODO: this should not be here!
        if (type == 1) {
            response.addHeader("X-JHT-Session-Id", session.getId());
            response.commit();
        } else if (type == 2) {
            response.commit();
        } else if (type == 3) {
            InputStream in2 = session.getSession().getInputStream();
            OutputStream out = response.getOutputStream("application/octet-stream");
            // TODO: loop here unless available() return 0
            byte[] buffer = new byte[4096];
            int c = in2.read(buffer);
            out.write(buffer, 0, c);
            out.close();
        }
    }
}
