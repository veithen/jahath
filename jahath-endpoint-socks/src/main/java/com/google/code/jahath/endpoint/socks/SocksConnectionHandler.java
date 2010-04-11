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
package com.google.code.jahath.endpoint.socks;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.Connection;
import com.google.code.jahath.Gateway;
import com.google.code.jahath.common.ConnectionRelay;
import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.socks.SocksConstants;
import com.google.code.jahath.common.socks.SocksDataInputStream;
import com.google.code.jahath.common.socks.SocksDataOutputStream;

public class SocksConnectionHandler implements ConnectionHandler {
    private static final Logger log = Logger.getLogger(SocksConnectionHandler.class.getName());
    
    private final Gateway gateway;
    
    public SocksConnectionHandler(Gateway gateway) {
        this.gateway = gateway;
    }

    public void handle(ExecutionEnvironment env, Connection connection) {
        try {
            boolean fineEnabled = log.isLoggable(Level.FINE);
            
            SocksDataInputStream in = new SocksDataInputStream(connection.getInputStream());
            SocksDataOutputStream out = new SocksDataOutputStream(new BufferedOutputStream(connection.getOutputStream(), 64));
            
            if (fineEnabled) {
                log.fine("Waiting for SOCKS request");
            }
            if (in.readByte() != SocksConstants.SOCKS_VERSION) {
                return; // TODO
            }
            int authMethodCount = in.readUnsignedByte();
            byte[] authMethods = new byte[authMethodCount];
            in.readFully(authMethods);
            // TODO: process proposed authentication methods
            
            out.writeByte(SocksConstants.SOCKS_VERSION);
            out.writeByte(SocksConstants.AUTH_USERNAME_PASSWORD);
            out.flush();
            
            if (fineEnabled) {
                log.fine("Waiting for SOCKS authentication");
            }
            if (in.readByte() != SocksConstants.USERNAME_PASSWORD_AUTH_VERSION) {
                return; // TODO
            }
            String username = in.readASCII();
            String password = in.readASCII();
            // TODO: authenticate
            out.writeByte(SocksConstants.USERNAME_PASSWORD_AUTH_VERSION);
            out.writeByte(0);
            out.flush();
            
            if (fineEnabled) {
                log.fine("Waiting for SOCKS connection request");
            }
            if (in.readByte() != SocksConstants.SOCKS_VERSION) {
                return; // TODO
            }
            byte requestType = in.readByte();
            if (in.readByte() != 0) {
                return; // TODO
            }
            InetSocketAddress destination = in.readSocketAddress();
            
            Connection targetConnection = gateway.connect(destination);
            if (fineEnabled) {
                log.fine("Connected to " + destination);
            }
            
            out.writeByte(SocksConstants.SOCKS_VERSION);
            out.writeByte(SocksConstants.STATUS_OK);
            out.writeByte(0);
            out.writeSocketAddress(destination);
            out.flush();
            if (fineEnabled) {
                log.fine("End processing SOCKS request");
            }
            
            new ConnectionRelay(log, env, connection, "socks", targetConnection, destination.toString()).run();
        } catch (IOException ex) {
            ex.printStackTrace(); // TODO
        }
    }
}
