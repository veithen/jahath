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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.jahath.common.StreamRelay;

class ConnectionHandler implements Runnable {
    private static final Log log = LogFactory.getLog(ConnectionHandler.class);
    
    private final Socket socket;
    private final Tunnel tunnel;
    
    public ConnectionHandler(Socket socket, Tunnel tunnel) {
        this.socket = socket;
        this.tunnel = tunnel;
    }

    public void run() {
        try {
            JahathClient client = tunnel.getClient();
            HttpRequest httpRequest = client.createRequest(HttpRequest.Method.POST, "/");
            httpRequest.addHeader("X-JHT-Remote-Host", tunnel.getRemoteHost());
            httpRequest.addIntHeader("X-JHT-Remote-Port", tunnel.getRemotePort());
            OutputStream out = httpRequest.getOutputStream("application/octet-stream");
            InputStream in = httpRequest.getResponse().getInputStream();
            ExecutorService executorService = client.getExecutorService();
            Future<?> f1 = executorService.submit(new StreamRelay("request", socket.getInputStream(), out));
            Future<?> f2 = executorService.submit(new StreamRelay("response", in, socket.getOutputStream()));
            f1.get();
            f2.get();
            socket.close();
        } catch (Exception ex) {
            log.error("", ex);
        }
    }
}
