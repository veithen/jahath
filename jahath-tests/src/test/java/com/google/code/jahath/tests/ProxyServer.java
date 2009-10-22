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
package com.google.code.jahath.tests;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.http.handler.ProxyHandler;
import org.mortbay.jetty.Server;

public class ProxyServer {
    private final Server server;
    final AtomicInteger requestCount = new AtomicInteger();
    
    public ProxyServer(int port) throws Exception {
        server = new Server();
        SocketListener listener = new SocketListener();
        listener.setPort(port);
        server.addListener(listener);
        HttpContext context = new HttpContext(server, "/");
        context.addHandler(new AbstractHttpHandler() {
            public void handle(String pathInContext, String pathParams, HttpRequest request,
                    HttpResponse response) throws HttpException, IOException {
                requestCount.incrementAndGet();
            }
        });
        context.addHandler(new ProxyHandler());
        server.start();
    }
    
    public void stop() throws Exception {
        server.stop();
    }
    
    public int getRequestCount() {
        return requestCount.get();
    }

    public static void main(String[] args) throws Exception {
        new ProxyServer(3128);
    }
}
