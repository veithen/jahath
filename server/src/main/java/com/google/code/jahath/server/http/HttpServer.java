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
package com.google.code.jahath.server.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

public class HttpServer {
    private final Acceptor acceptor;
    
    public HttpServer(int port, ExecutorService executorService, HttpRequestHandler requestHandler) throws IOException {
        acceptor = new Acceptor(new ServerSocket(port), executorService, requestHandler);
        executorService.execute(acceptor);
    }

    public final void stop() {
        acceptor.stop();
    }
}
