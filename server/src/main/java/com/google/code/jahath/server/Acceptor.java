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

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import com.google.code.jahath.common.AbstractAcceptor;

class Acceptor extends AbstractAcceptor {
    private final ExecutorService executorService;

    public Acceptor(ServerSocket serverSocket, ExecutorService executorService) {
        super(serverSocket);
        this.executorService = executorService;
    }

    @Override
    protected void handleConnection(Socket socket) {
        executorService.execute(new ConnectionHandler(socket, executorService));
    }
}
