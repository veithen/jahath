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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.container.Task;
import com.google.code.jahath.server.http.HttpRequest;
import com.google.code.jahath.server.http.HttpRequestHandler;
import com.google.code.jahath.server.http.HttpResponse;

class HttpRequestHandlerImpl implements HttpRequestHandler {
    static final Logger log = Logger.getLogger(HttpRequestHandlerImpl.class.getName());
    
    private final ConnectionHandler connectionHandler;
    private final Map<String,ConnectionImpl> connections = Collections.synchronizedMap(new HashMap<String,ConnectionImpl>());

    public HttpRequestHandlerImpl(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    private ConnectionImpl createConnection(final ExecutionEnvironment env) throws IOException {
        final String id = UUID.randomUUID().toString();
        if (log.isLoggable(Level.FINE)) {
            log.fine("Setting up new connection " + id);
        }
        final ConnectionHandler connectionHandler = this.connectionHandler;
        final ConnectionImpl connection = new ConnectionImpl(id);
        connections.put(id, connection);
        env.execute(new Task() {
            public void run() {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Running " + connectionHandler.getClass().getName() + " on connection " + id);
                }
                connectionHandler.handle(env, connection);
            }

            public void stop() {
                // TODO
            }
        });
        return connection;
    }
    
    private ConnectionImpl getConnection(String id) {
        return connections.get(id);
    }
    
    public void handle(ExecutionEnvironment env, HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        if (log.isLoggable(Level.FINE)) {
            log.fine("Processing request for path " + path);
        }
        if (path.equals("/")) {
        	handleConnect(createConnection(env), request, response);
        } else {
            String connectionId = path.substring(1);
            ConnectionImpl connection = getConnection(connectionId);
            if (request.getHeader("Content-Type") != null) {
            	handleSend(connection, request, response);
            } else {
            	handleReceive(connection, request, response);
            }
        }
    }
    
    private void handleConnect(ConnectionImpl connection, HttpRequest request, HttpResponse response) throws IOException {
        response.setStatus(204);
        response.addHeader("X-JHT-Connection-Id", connection.getId());
        response.commit();
    }
    
    private void handleSend(ConnectionImpl connection, HttpRequest request, HttpResponse response) throws IOException {
        InputStream data = request.getInputStream();
        if (data == null) {
            log.info("Protocol error: request didn't contain data");
            response.setStatus(400);
        } else {
            try {
                connection.consume(request.getInputStream());
            } catch (InterruptedException ex) {
                // TODO: check what to do here
                Thread.currentThread().interrupt();
            }
            response.setStatus(202);
        }
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
