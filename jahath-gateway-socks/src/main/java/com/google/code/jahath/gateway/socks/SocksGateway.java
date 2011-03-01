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
package com.google.code.jahath.gateway.socks;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.Endpoint;
import com.google.code.jahath.common.socks.SocksConstants;
import com.google.code.jahath.common.socks.SocksDataInputStream;
import com.google.code.jahath.common.socks.SocksDataOutputStream;
import com.google.code.jahath.tcp.Gateway;
import com.google.code.jahath.tcp.SocketAddress;
import com.googlecode.sisme.stream.Connection;

public class SocksGateway implements Gateway {
    private static final Logger log = Logger.getLogger(SocksGateway.class.getName());
    
    private final ServiceTracker endpointTracker;
    
    public SocksGateway(BundleContext bundleContext, String endpointName) {
        try {
            endpointTracker = new ServiceTracker(bundleContext,
                    bundleContext.createFilter("(&(objectClass=" + Endpoint.class.getName() + ")(name=" + endpointName + "))"), null);
            endpointTracker.open();
        } catch (InvalidSyntaxException ex) {
            throw new Error(ex);
        }
    }

    public Connection connect(SocketAddress socketAddress) throws IOException {
        Endpoint endpoint = (Endpoint)endpointTracker.getService();
        if (endpoint == null) {
            throw new IOException("Endpoint not available");
        }
        Connection connection = endpoint.connect();
        SocksDataOutputStream out = new SocksDataOutputStream(new BufferedOutputStream(connection.getOutputStream(), 64));
        SocksDataInputStream in = new SocksDataInputStream(connection.getInputStream());
        
        out.writeByte(SocksConstants.SOCKS_VERSION);
        out.writeByte(2);
        out.writeByte(SocksConstants.AUTH_NONE);
        out.writeByte(SocksConstants.AUTH_USERNAME_PASSWORD);
        out.flush();
        
        if (in.readByte() != SocksConstants.SOCKS_VERSION) {
            throw new IOException("Unexpected SOCKS version");
        }
        byte authProtocol = in.readByte();
        
        if (log.isLoggable(Level.FINE)) {
            log.fine("Authenticating using protocol: " + SocksConstants.getAuthProtocolDescription(authProtocol));
        }
        switch (authProtocol) {
            case SocksConstants.AUTH_NONE:
                // TODO
                break;
            case SocksConstants.AUTH_USERNAME_PASSWORD:
                out.writeByte(SocksConstants.USERNAME_PASSWORD_AUTH_VERSION);
                out.writeASCII("username"); // TODO
                out.writeASCII("password"); // TODO
                break;
            default:
                throw new IOException("Server requested unsupported authentication protocol");
        }
        out.flush();
        
        if (in.readByte() != SocksConstants.USERNAME_PASSWORD_AUTH_VERSION) {
            throw new IOException(); // TODO
        }
        if (in.readByte() != 0) {
            throw new IOException(); // TODO
        }
        
        out.writeByte(SocksConstants.SOCKS_VERSION);
        out.writeByte(SocksConstants.REQUEST_TCP_CONNECTION);
        out.writeByte(0);
        out.writeSocketAddress(socketAddress);
        out.flush();
        
        if (in.readByte() != SocksConstants.SOCKS_VERSION) {
            throw new IOException(); // TODO
        }
        if (in.readByte() != SocksConstants.STATUS_OK) {
            throw new IOException(); // TODO
        }
        if (in.readByte() != 0) {
            throw new IOException(); // TODO
        }
        SocketAddress target2 = in.readSocketAddress();
        if (log.isLoggable(Level.FINE)) {
            log.fine("SOCKS server connected to " + target2);
        }
        
        return connection;
    }
    
    public void destroy() {
        endpointTracker.close();
    }
}
