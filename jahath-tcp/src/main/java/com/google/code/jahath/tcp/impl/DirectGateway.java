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
package com.google.code.jahath.tcp.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.DnsAddress;
import com.google.code.jahath.HostAddress;
import com.google.code.jahath.IPAddress;
import com.google.code.jahath.common.connection.SocketConnection;
import com.google.code.jahath.resolver.Resolver;
import com.google.code.jahath.tcp.Gateway;
import com.google.code.jahath.tcp.SocketAddress;
import com.googlecode.sisme.stream.Connection;

public class DirectGateway implements Gateway {
    private final ServiceTracker resolverTracker;
    
    public DirectGateway(BundleContext bundleContext) {
        resolverTracker = new ServiceTracker(bundleContext, Resolver.class.getName(), null);
        resolverTracker.open();
    }
    
    public Connection connect(SocketAddress socketAddress) throws IOException {
        HostAddress host = socketAddress.getHost();
        IPAddress ip;
        if (host instanceof IPAddress) {
            ip = (IPAddress)host;
        } else {
            Resolver resolver = (Resolver)resolverTracker.getService();
            if (resolver == null) {
                throw new UnknownHostException(host.toString());
            } else {
                ip = resolver.resolve((DnsAddress)host);
                if (ip == null) {
                    throw new UnknownHostException(host.toString());
                }
            }
        }
        return new SocketConnection(new Socket(ip.toInetAddress(), socketAddress.getPort()));
    }
}
