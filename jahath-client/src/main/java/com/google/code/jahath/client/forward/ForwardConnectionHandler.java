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
package com.google.code.jahath.client.forward;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.code.jahath.client.VCHClient;
import com.google.code.jahath.common.ConnectionRelay;
import com.google.code.jahath.common.connection.Connection;
import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.common.container.ExecutionEnvironment;

class ForwardConnectionHandler implements ConnectionHandler {
    private static final Logger log = Logger.getLogger(ForwardConnectionHandler.class.getName());
    
    private final VCHClient client;
    
    public ForwardConnectionHandler(VCHClient client) {
        this.client = client;
    }

    public void handle(ExecutionEnvironment env, Connection connection) {
        try {
            // TODO: generate proper labels
            new ConnectionRelay(log, env, connection, "???", client.createConnection(), "jahath-server");
        } catch (IOException ex) {
            // TODO
            ex.printStackTrace();
        }
    }
}
