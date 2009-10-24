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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.connection.Connection;
import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.http.HttpHeadersProvider;
import com.google.code.jahath.common.http.HttpOutMessage;
import com.google.code.jahath.common.http.HttpOutputStream;

class HttpConnectionHandler implements ConnectionHandler, HttpHeadersProvider {
    private static final Logger log = Logger.getLogger(HttpConnectionHandler.class.getName());
    
    private final HttpRequestHandler requestHandler;

    public HttpConnectionHandler(HttpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void writeHeaders(HttpOutMessage message) throws IOException {
        message.addHeader("Connection", "keep-alive");
    }

    public void handle(ExecutionEnvironment env, Connection connection) {
        try {
            log.fine("New request");
            // TODO: should wrapping the stream as an HttpOutputStream be done here or in HttpRequest??
            HttpOutputStream response = new HttpOutputStream(connection.getOutputStream());
            HttpRequest httpRequest = new HttpRequest(connection.getInputStream());
            HttpResponse httpResponse = new HttpResponse(response, this);
            requestHandler.handle(env, httpRequest, httpResponse);
            response.flush();
            // TODO: equivalent for Connection
//            socket.close();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
        }
    }
}
