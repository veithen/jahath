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
package com.google.code.jahath.client;

import java.net.ServerSocket;
import java.net.Socket;

import com.google.code.jahath.common.AbstractAcceptor;

class Acceptor extends AbstractAcceptor {
    private final Tunnel tunnel;

    public Acceptor(ServerSocket serverSocket, Tunnel tunnel) {
        super(serverSocket);
        this.tunnel = tunnel;
    }

    @Override
    protected void handleConnection(Socket socket) {
        tunnel.getClient().getExecutorService().execute(new ConnectionHandler(socket, tunnel));
    }
}
