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

import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.Connection;
import com.google.code.jahath.Endpoint;
import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.http.HttpRequest;
import com.google.code.jahath.tcp.SocketAddress;

public class HttpProxyGateway extends AbstractHttpGateway {
    private final ServiceTracker endpointTracker;
    
    public HttpProxyGateway() {
        
    }

    @Override
    protected Connection createHttpConnection(SocketAddress server) throws IOException {
        Endpoint endpoint = (Endpoint)endpointTracker.getService();
        if (endpoint == null) {
            throw new Error("unavailable"); // TODO
        } else {
            return endpoint.connect();
        }
    }

    @Override
    protected String getRequestTarget(SocketAddress server, String path) {
        return "http://" + getHostHeader(server) + path;
    }

    @Override
    protected void addHeaders(HttpRequest request) throws HttpException {
        request.addHeader(HttpConstants.Headers.PROXY_CONNECTION, "keep-alive");
    }
}
