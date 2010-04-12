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
package com.google.code.jahath.common.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.connection.Endpoint;
import com.google.code.jahath.common.connection.ConnectionHandlerTask;
import com.google.code.jahath.common.connection.SocketConnection;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.container.Task;

class Acceptor implements Task {
    static final Logger log = Logger.getLogger(Acceptor.class.getName());

    private final ServerSocket serverSocket;
    final ExecutionEnvironment env;
    final Endpoint endpoint;

    public Acceptor(ServerSocket serverSocket, ExecutionEnvironment env, Endpoint endpoint) {
        this.serverSocket = serverSocket;
        this.env = env;
        this.endpoint = endpoint;
    }
    
    public final void run() {
        try {
            while (true) {
                final Socket socket = serverSocket.accept();
                env.execute(new ConnectionHandlerTask(endpoint, env, new SocketConnection(socket)));
            }
        } catch (IOException ex) {
            if (!serverSocket.isClosed()) {
                log.log(Level.SEVERE, "Unexpected I/O error in accept loop", ex);
            }
        }
    }
    
    public final void stop() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error closing server socket", ex);
        }
    }
}
