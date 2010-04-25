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

import java.io.IOException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.Connection;
import com.google.code.jahath.Endpoint;
import com.google.code.jahath.http.HttpGateway;
import com.google.code.jahath.tcp.SocketAddress;

public class VCHEndpoint implements Endpoint {
    private final SocketAddress server;
    private final String serviceName;
    private final ServiceTracker httpGatewayTracker;

    public VCHEndpoint(BundleContext bundleContext, SocketAddress server, String serviceName, String httpGatewayName) {
        this.server = server;
        this.serviceName = serviceName;
        try {
            httpGatewayTracker = new ServiceTracker(bundleContext,
                    bundleContext.createFilter("(&(objectClass=" + HttpGateway.class.getName() + ")(name=" + httpGatewayName + "))"), null);
            httpGatewayTracker.open();
        } catch (InvalidSyntaxException ex) {
            throw new Error(ex);
        }
    }
    
    public Connection connect() throws IOException {
        HttpGateway httpGateway = (HttpGateway)httpGatewayTracker.getService();
        if (httpGateway == null) {
            throw new IOException("Gateway not available");
        } else {
            return new VCHClient(server, httpGateway).createConnection(serviceName);
        }
    }
    
    public void destroy() {
        httpGatewayTracker.close();
    }
}
