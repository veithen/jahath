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

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.CRLFOutputStream;
import com.google.code.jahath.common.ChunkedInputStream;
import com.google.code.jahath.common.ChunkedOutputStream;
import com.google.code.jahath.common.Relay;

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
            Socket outSocket = new Socket(client.getServerHost(), client.getServerPort());
            CRLFOutputStream request = new CRLFOutputStream(outSocket.getOutputStream());
            CRLFInputStream response = new CRLFInputStream(outSocket.getInputStream());
            request.writeLine("POST / HTTP/1.1");
            request.writeLine("Host: " + client.getServerHost() + ":" + client.getServerPort());
            request.writeLine("Content-Type: application/octet-stream");
            request.writeLine("Transfer-Encoding: chunked");
            request.writeLine("Connection: keep-alive");
            request.writeLine("X-JHT-Remote-Host: " + tunnel.getRemoteHost());
            request.writeLine("X-JHT-Remote-Port: " + tunnel.getRemotePort());
            request.writeLine("");
            String status = response.readLine();
            log.info(status);
            // TODO: process status line
            String header;
            while ((header = response.readLine()).length() > 0) {
                // TODO: process header line
            }
            ExecutorService executorService = client.getExecutorService();
            Future<?> f1 = executorService.submit(new Relay("request", socket.getInputStream(), new ChunkedOutputStream(request)));
            Future<?> f2 = executorService.submit(new Relay("response", new ChunkedInputStream(response), socket.getOutputStream()));
            f1.get();
            f2.get();
            socket.close();
        } catch (Exception ex) {
            log.error("", ex);
        }
    }
}
