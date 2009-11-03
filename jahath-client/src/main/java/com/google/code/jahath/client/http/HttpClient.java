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
package com.google.code.jahath.client.http;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.LogUtil;
import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpOutputStream;

public class HttpClient {
    private static final Logger log = Logger.getLogger(HttpClient.class.getName());
    
    private final String serverHost;
    private final int serverPort;
    private final ProxyConfiguration proxyConfiguration;

    public HttpClient(String serverHost, int serverPort, ProxyConfiguration proxyConfiguration) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.proxyConfiguration = proxyConfiguration;
    }

    public HttpRequest createRequest(HttpRequest.Method method, String path) throws IOException {
        Socket socket;
        if (proxyConfiguration == null) {
            socket = new Socket(serverHost, serverPort);
        } else {
            socket = new Socket(proxyConfiguration.getHost(), proxyConfiguration.getPort());
        }
        HttpOutputStream request = new HttpOutputStream(LogUtil.log(socket.getOutputStream(), log, Level.FINER, "HTTP request"));
        String address = serverPort == 80 ? serverHost : serverHost + ":" + serverPort;
        if (proxyConfiguration == null) {
            request.writeLine(method + " " + path + " HTTP/1.1");
        } else {
            request.writeLine(method + " http://" + address + path + " HTTP/1.1");
        }
        HttpRequest httpRequest = new HttpRequest(request, LogUtil.log(socket.getInputStream(), log, Level.FINER, "HTTP response"));
        httpRequest.addHeader(HttpConstants.H_HOST, address);
        httpRequest.addHeader(HttpConstants.H_CONNECTION, "keep-alive");
        if (proxyConfiguration != null) {
            httpRequest.addHeader(HttpConstants.H_PROXY_CONNECTION, "keep-alive");
        }
        return httpRequest;
    }
}
