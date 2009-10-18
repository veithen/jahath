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

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.CRLFOutputStream;
import com.google.code.jahath.common.ChunkedInputStream;
import com.google.code.jahath.common.ChunkedOutputStream;
import com.google.code.jahath.common.Headers;
import com.google.code.jahath.common.Relay;

class ConnectionHandler implements Runnable {
    private static final Log log = LogFactory.getLog(ConnectionHandler.class);
    
    private final Socket socket;
    private final ExecutorService executorService;

    public ConnectionHandler(Socket socket, ExecutorService executorService) {
        this.socket = socket;
        this.executorService = executorService;
    }

    public void run() {
        try {
            CRLFInputStream request = new CRLFInputStream(socket.getInputStream());
            CRLFOutputStream response = new CRLFOutputStream(socket.getOutputStream());
            String requestLine = request.readLine();
            log.info(requestLine);
            // TODO: process request line
            Headers headers = new Headers(request);
            // TODO: process all relevant headers!
            String targetHost = headers.getHeader("X-JHT-Remote-Host");
            int targetPort = headers.getIntHeader("X-JHT-Remote-Port");
            response.writeLine("HTTP/1.1 200 OK");
            response.writeLine("Content-Type: application/octet-stream");
            response.writeLine("Transfer-Encoding: chunked");
            response.writeLine("Connection: keep-alive");
            response.writeLine("");
            response.flush();
            Socket outSocket = new Socket(targetHost, targetPort);
            String remoteHost = socket.getInetAddress().getHostName();
            Future<?> f1 = executorService.submit(new Relay(remoteHost + " -> " + targetHost + ":" + targetPort, new ChunkedInputStream(request), outSocket.getOutputStream()));
            Future<?> f2 = executorService.submit(new Relay(targetHost + ":" + targetPort + " -> " + remoteHost, outSocket.getInputStream(), new ChunkedOutputStream(response)));
            try {
                f1.get();
                f2.get();
            } catch (Exception ex) {
                log.error("", ex);
            }
            socket.close();
        } catch (Exception ex) {
            log.error("", ex);
        }
    }
}
