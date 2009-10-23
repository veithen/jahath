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
package com.google.code.jahath.client.tunnel;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.client.JahathClient;
import com.google.code.jahath.common.ConnectionRelay;
import com.google.code.jahath.common.connection.Connection;
import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.common.connection.ExecutionEnvironment;
import com.google.code.jahath.common.socks.SocksConstants;
import com.google.code.jahath.common.socks.SocksDataInputStream;
import com.google.code.jahath.common.socks.SocksDataOutputStream;

class TunnelConnectionHandler implements ConnectionHandler {
    private static final Logger log = Logger.getLogger(TunnelConnectionHandler.class.getName());
    
    private final JahathClient client;
    private final InetSocketAddress target;
    
    public TunnelConnectionHandler(JahathClient client, InetSocketAddress target) {
        this.client = client;
        this.target = target;
    }

    public void handle(ExecutionEnvironment env, Connection inConnection) {
        try {
            Connection outConnection = client.createConnection();
            SocksDataOutputStream out = new SocksDataOutputStream(new BufferedOutputStream(outConnection.getOutputStream(), 64));
            SocksDataInputStream in = new SocksDataInputStream(outConnection.getInputStream());
            
            out.writeByte(SocksConstants.SOCKS_VERSION);
            out.writeByte(2);
            out.writeByte(SocksConstants.AUTH_NONE);
            out.writeByte(SocksConstants.AUTH_USERNAME_PASSWORD);
            out.flush();
            
            if (in.readByte() != SocksConstants.SOCKS_VERSION) {
                return; // TODO
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
                    return; // TODO
            }
            out.flush();
            
            if (in.readByte() != SocksConstants.USERNAME_PASSWORD_AUTH_VERSION) {
                return; // TODO
            }
            if (in.readByte() != 0) {
                return; // TODO
            }
            
            out.writeByte(SocksConstants.SOCKS_VERSION);
            out.writeByte(SocksConstants.REQUEST_TCP_CONNECTION);
            out.writeByte(0);
            out.writeSocketAddress(target);
            out.flush();
            
            if (in.readByte() != SocksConstants.SOCKS_VERSION) {
                return; // TODO
            }
            if (in.readByte() != SocksConstants.STATUS_OK) {
                return; // TODO
            }
            if (in.readByte() != 0) {
                return; // TODO
            }
            InetSocketAddress target2 = in.readSocketAddress();
            if (log.isLoggable(Level.FINE)) {
                log.fine("SOCKS server connected to " + target2);
            }
            
            // TODO: generate proper labels
            new ConnectionRelay(log, env.getExecutorService(), inConnection, "???", outConnection, "jahath-server").run();
        } catch (IOException ex) {
            // TODO
            ex.printStackTrace();
        }
    }
}
