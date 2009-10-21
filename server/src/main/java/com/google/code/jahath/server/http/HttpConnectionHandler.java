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
import java.io.InputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.http.HttpHeadersProvider;
import com.google.code.jahath.common.http.HttpOutMessage;
import com.google.code.jahath.common.http.HttpOutputStream;

class HttpConnectionHandler implements Runnable, HttpHeadersProvider {
    private static final Log log = LogFactory.getLog(HttpConnectionHandler.class);
    
    private final Socket socket;
    private final HttpRequestHandler requestHandler;

    public HttpConnectionHandler(Socket socket, HttpRequestHandler requestHandler) {
        this.socket = socket;
        this.requestHandler = requestHandler;
    }

    public void writeHeaders(HttpOutMessage message) throws IOException {
        message.addHeader("Connection", "keep-alive");
    }

    public void run() {
        try {
            CRLFInputStream request = new CRLFInputStream(socket.getInputStream());
            HttpOutputStream response = new HttpOutputStream(socket.getOutputStream());
            String requestLine = request.readLine();
            // TODO: do this properly!
            String[] parts = requestLine.split(" ");
            String path = parts[1];
            InputStream in;
            HttpRequest httpRequest = new HttpRequest(path, request);
            HttpResponse httpResponse = new HttpResponse(response, this);
            requestHandler.handle(httpRequest, httpResponse);
            response.flush();
            socket.close();
        } catch (Exception ex) {
            log.error("", ex);
        }
    }
}
