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
package com.google.code.jahath.http.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.LogUtil;
import com.google.code.jahath.common.http.HttpConnectionException;
import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpOutputStream;
import com.google.code.jahath.http.HttpGateway;
import com.google.code.jahath.http.HttpRequest;
import com.google.code.jahath.tcp.SocketAddress;
import com.googlecode.sisme.stream.Connection;

public abstract class AbstractHttpGateway implements HttpGateway {
    private static final Logger log = Logger.getLogger(AbstractHttpGateway.class.getName());
    
    public HttpRequest createRequest(HttpRequest.Method method, SocketAddress server, String path) throws HttpException {
        try {
            Connection connection = createHttpConnection(server);
            HttpOutputStream request = new HttpOutputStream(LogUtil.log(connection.getStreamSink().getOutputStream(), log, Level.FINER, "HTTP request"));
            request.writeLine(method + " " + getRequestTarget(server, path) + " " + HttpConstants.HTTP_VERSION_1_1);
            HttpRequest httpRequest = new HttpRequestImpl(request, LogUtil.log(connection.getStreamSource().getInputStream(), log, Level.FINER, "HTTP response"));
            httpRequest.addHeader(HttpConstants.Headers.HOST, getHostHeader(server));
            httpRequest.addHeader(HttpConstants.Headers.CONNECTION, "keep-alive");
            addHeaders(httpRequest);
            return httpRequest;
        } catch (IOException ex) {
            throw new HttpConnectionException(ex);
        }
    }
    
    protected abstract Connection createHttpConnection(SocketAddress server) throws IOException;
    
    protected abstract String getRequestTarget(SocketAddress server, String path);
    
    protected abstract void addHeaders(HttpRequest request) throws HttpException;
    
    protected final String getHostHeader(SocketAddress server) {
        int serverPort = server.getPort();
        String serverHost = server.getHost().toString();
        return serverPort == 80 ? serverHost : serverHost + ":" + serverPort;
    }
}
