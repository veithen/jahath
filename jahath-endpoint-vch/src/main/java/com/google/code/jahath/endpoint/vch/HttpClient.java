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
package com.google.code.jahath.endpoint.vch;

import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.http.HttpGateway;
import com.google.code.jahath.http.HttpRequest;
import com.google.code.jahath.tcp.SocketAddress;

public class HttpClient {
    private final SocketAddress server;
    private final HttpGateway gateway;

    public HttpClient(SocketAddress server, HttpGateway gateway) {
        this.server = server;
        this.gateway = gateway;
    }

    public HttpRequest createRequest(HttpRequest.Method method, String path) throws HttpException {
        return gateway.createRequest(method, server, path);
    }
    
    private String getHostHeader() {
        String serverHost = server.getHost().toString();
        int serverPort = server.getPort();
        return serverPort == 80 ? serverHost : serverHost + ":" + serverPort;
    }
    
    private String getServerUrl() {
        return "http://" + getHostHeader();
    }
    
    public String getPath(String url) {
        String serverUrl = getServerUrl();
        int len = serverUrl.length();
        if (url.length() > len && url.startsWith(serverUrl) && url.charAt(len) == '/') {
            return url.substring(len);
        } else {
            return null;
        }
    }
}
