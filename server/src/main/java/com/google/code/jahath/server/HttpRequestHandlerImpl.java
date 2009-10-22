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
import java.io.OutputStream;

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
        if (path.equals("/")) {
        	handleConnect(server.createConnection(), request, response);
        } else {
            String connectionId = path.substring(1);
            ConnectionImpl connection = server.getConnection(connectionId);
            if (request.getHeader("Content-Type") != null) {
            	handleSend(connection, request, response);
            } else {
            	handleReceive(connection, request, response);
            }
        }
    }
    
    private void handleConnect(ConnectionImpl connection, HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(200);
        response.addHeader("X-JHT-Connection-Id", connection.getId());
        response.commit();
    }
    
    private void handleSend(ConnectionImpl connection, HttpRequest request, HttpResponse response) throws IOException {
        try {
            connection.consume(request.getInputStream());
        } catch (InterruptedException ex) {
            // TODO: check what to do here
            Thread.currentThread().interrupt();
        }
        response.setStatus(200);
        response.commit();
    }
    
    private void handleReceive(ConnectionImpl connection, HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(200);
        OutputStream out = response.getOutputStream("application/octet-stream");
        try {
            connection.produce(out);
        } catch (InterruptedException ex) {
            // TODO: check what to do here
            Thread.currentThread().interrupt();
        }
        out.close();
    }
}
