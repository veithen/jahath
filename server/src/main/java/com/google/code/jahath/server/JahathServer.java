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
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpHandler;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.jetty.Server;

import com.google.code.jahath.common.Relay;

public class JahathServer {
    private static final Log log = LogFactory.getLog(JahathServer.class);
    
    private final Server server;
    
    public JahathServer(int port) throws Exception {
        final ExecutorService executorService = new ThreadPoolExecutor(6, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        server = new Server();
        SocketListener listener = new SocketListener();
        listener.setPort(port);
        server.addListener(listener);
        HttpContext context = new HttpContext(server, "/");
        HttpHandler handler = new AbstractHttpHandler() {
            public void handle(String pathInContext, String pathParams,
                    HttpRequest request, HttpResponse response) throws HttpException,
                    IOException {
                String remoteHost = request.getRemoteHost();
                String targetHost = request.getField("X-JHT-Remote-Host");
                int targetPort = request.getIntField("X-JHT-Remote-Port");
                Socket socket = new Socket(targetHost, targetPort);
                response.commit();
                Future<?> f1 = executorService.submit(new Relay(remoteHost + " -> " + targetHost + ":" + targetPort, request.getInputStream(), socket.getOutputStream()));
                Future<?> f2 = executorService.submit(new Relay(targetHost + ":" + targetPort + " -> " + remoteHost, socket.getInputStream(), response.getOutputStream()));
                try {
                    f1.get();
                    f2.get();
                } catch (Exception ex) {
                    log.error("", ex);
                }
                log.info("Handled");
                request.setHandled(true);
            }
        };
        context.addHandler(handler);
        server.start();
    }

    public final void stop() throws InterruptedException {
        server.stop();
    }
}
