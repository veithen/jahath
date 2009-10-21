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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.http.HttpOutputStream;

public class JahathClient {
    private final String serverHost;
    private final int serverPort;
    private final ProxyConfiguration proxyConfiguration;
    private final ExecutorService executorService;
    
    public JahathClient(String serverHost, int serverPort, ProxyConfiguration proxyConfiguration) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.proxyConfiguration = proxyConfiguration;
        executorService = new ThreadPoolExecutor(6, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    ProxyConfiguration getProxyConfiguration() {
        return proxyConfiguration;
    }

    ExecutorService getExecutorService() {
        return executorService;
    }
    
    HttpRequest createRequest(HttpRequest.Method method, String path) throws IOException {
        Socket socket;
        if (proxyConfiguration == null) {
            socket = new Socket(serverHost, serverPort);
        } else {
            socket = new Socket(proxyConfiguration.getHost(), proxyConfiguration.getPort());
        }
        HttpOutputStream request = new HttpOutputStream(socket.getOutputStream());
        String address = serverPort == 80 ? serverHost : serverHost + ":" + serverPort;
        if (proxyConfiguration == null) {
            request.writeLine(method + " " + path + " HTTP/1.1");
        } else {
            request.writeLine(method + " http://" + address + path + " HTTP/1.1");
        }
        HttpRequest httpRequest = new HttpRequest(request, socket.getInputStream());
        httpRequest.addHeader("Host", address);
        httpRequest.addHeader("Connection", "keep-alive");
        if (proxyConfiguration != null) {
            httpRequest.addHeader("Proxy-Connection", "keep-alive");
        }
        return httpRequest;
    }

    public Session createSession() throws IOException {
        HttpRequest request = createRequest(HttpRequest.Method.POST, "/");
        HttpResponse response = request.execute();
        String sessionId = response.getHeader("X-JHT-Session-Id");
        return new Session(this, sessionId);
    }

    public Tunnel createTunnel(int port, String remoteHost, int remotePort) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        Tunnel tunnel = new Tunnel(this, remoteHost, remotePort);
        Acceptor acceptor = new Acceptor(ss, tunnel);
        tunnel.setAcceptor(acceptor);
        executorService.execute(acceptor);
        return tunnel;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
