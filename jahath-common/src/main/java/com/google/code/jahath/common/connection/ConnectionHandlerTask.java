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
package com.google.code.jahath.common.connection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.container.Task;
import com.googlecode.sisme.stream.Connection;

public class ConnectionHandlerTask implements Task {
    private static final Logger log = Logger.getLogger(ConnectionHandlerTask.class.getName());

    private final Service service;
    private final ExecutionEnvironment env;
    private final Connection connection;
    
    public ConnectionHandlerTask(Service service, ExecutionEnvironment env, Connection connection) {
        this.service = service;
        this.env = env;
        this.connection = connection;
    }

    private void closeConnection() {
        if (connection.getState() == Connection.State.OPEN) {
            try {
                connection.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, "Error closing connection", ex);
            }
        }
    }
    
    public void run() {
        if (log.isLoggable(Level.FINE)) {
            // TODO: should we simply use Connection#toString or should we define a specific method in Connection?
            log.fine("Running " + service.getClass().getName() + " on connection " + connection);
        }
        try {
            service.handle(env, connection);
        } finally {
            closeConnection();
        }
    }

    public void stop() {
        closeConnection();
    }
}
