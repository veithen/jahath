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

import com.google.code.jahath.common.connection.Service;
import com.google.code.jahath.common.container.Container;
import com.google.code.jahath.common.container.ContainerFactory;
import com.google.code.jahath.common.container.ExecutionEnvironment;

public class Server {
    private final Container container;
    private final Acceptor acceptor;
    
    public Server(int port, Service service) throws IOException {
        container = ContainerFactory.createContainer("Server:" + port);
        ExecutionEnvironment env = container.getExecutionEnvironment();
        acceptor = new Acceptor(new ServerSocket(port), env, service);
        env.execute(acceptor);
    }

    public final void stop() throws InterruptedException {
        acceptor.stop(); // TODO: should no longer be necessary because it will be invoked during container shutdown
        container.shutdown();
    }
}
