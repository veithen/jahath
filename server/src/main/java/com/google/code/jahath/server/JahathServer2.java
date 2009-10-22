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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.server.http.HttpServer;

public class JahathServer2 {
    private final ConnectionHandler sessionHandler;
    private final ExecutorService executorService;
    private final HttpServer httpServer;
    private final Map<String,ConnectionImpl> connections = Collections.synchronizedMap(new HashMap<String,ConnectionImpl>());
    
    public JahathServer2(int port, ConnectionHandler connectionHandler) throws IOException {
        this.sessionHandler = connectionHandler;
        executorService = new ThreadPoolExecutor(6, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        httpServer = new HttpServer(port, executorService, new HttpRequestHandlerImpl(this));
    }

    ExecutorService getExecutorService() {
        return executorService;
    }

    ConnectionImpl createConnection() throws IOException {
        String id = UUID.randomUUID().toString();
        final ConnectionHandler connectionHandler = this.sessionHandler;
        final ConnectionImpl session = new ConnectionImpl(id);
        connections.put(id, session);
        executorService.execute(new Runnable() {
            public void run() {
                connectionHandler.handle(session);
            }
        });
        return session;
    }
    
    ConnectionImpl getConnection(String id) {
        return connections.get(id);
    }
    
    public final void stop() {
        httpServer.stop();
        executorService.shutdown();
    }
}
