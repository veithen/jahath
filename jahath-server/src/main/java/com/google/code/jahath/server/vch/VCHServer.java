/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.jahath.server.vch;

import java.io.IOException;

import com.google.code.jahath.common.connection.Endpoint;
import com.google.code.jahath.server.http.HttpServer;

public class VCHServer {
    private final ServiceRegistry serviceRegistry = new ServiceRegistry();
    private final HttpServer httpServer;
    
    public VCHServer(int port) throws IOException {
        httpServer = new HttpServer(port, new HttpRequestHandlerImpl(serviceRegistry));
    }

    public void registerService(String name, Endpoint endpoint) {
        serviceRegistry.registerService(name, endpoint);
    }

    public final void stop() throws InterruptedException {
        httpServer.stop();
    }
}
