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
package com.google.code.jahath.client.vch;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.code.jahath.client.http.HttpClient;
import com.google.code.jahath.client.http.HttpRequest;
import com.google.code.jahath.client.http.HttpResponse;
import com.google.code.jahath.client.http.ProxyConfiguration;
import com.google.code.jahath.client.http.HttpRequest.Method;
import com.google.code.jahath.common.connection.Connection;

public class VCHClient {
    private final HttpClient httpClient;
    private final ExecutorService executorService;
    
    public VCHClient(String serverHost, int serverPort, ProxyConfiguration proxyConfiguration) {
        httpClient = new HttpClient(serverHost, serverPort, proxyConfiguration);
        executorService = new ThreadPoolExecutor(6, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
    ExecutorService getExecutorService() {
        return executorService;
    }
    
    HttpRequest createRequest(Method method, String path) throws IOException {
        return httpClient.createRequest(method, path);
    }

    public Connection createConnection() throws IOException {
        HttpRequest request = createRequest(HttpRequest.Method.POST, "/");
        HttpResponse response = request.execute();
        String connectionId = response.getHeader("X-JHT-Connection-Id");
        return new ConnectionImpl(this, connectionId);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
