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
package com.google.code.jahath.server.socks;

import java.io.IOException;

import com.google.code.jahath.common.socks.SocksConstants;
import com.google.code.jahath.common.socks.SocksDataInputStream;
import com.google.code.jahath.common.socks.SocksDataOutputStream;
import com.google.code.jahath.server.Session;
import com.google.code.jahath.server.SessionHandler;

public class SocksSessionHandler implements SessionHandler {
    public void handle(Session session) {
        try {
            SocksDataInputStream in = new SocksDataInputStream(session.getInputStream());
            SocksDataOutputStream out = new SocksDataOutputStream(session.getOutputStream());
            byte version = in.readByte();
            if (version != SocksConstants.SOCKS_VERSION) {
                return; // TODO
            }
            int authMethodCount = in.readUnsignedByte();
            byte[] authMethods = new byte[authMethodCount];
            in.readFully(authMethods);
            // TODO: process proposed authentication methods
            out.writeByte(SocksConstants.SOCKS_VERSION);
            out.writeByte(SocksConstants.AUTH_USERNAME_PASSWORD);
            byte authVersion = in.readByte();
            if (authVersion != SocksConstants.USERNAME_PASSWORD_AUTH_VERSION) {
                return; // TODO
            }
            String username = in.readASCII();
            String password = in.readASCII();
            // TODO: authenticate
            out.writeByte(SocksConstants.USERNAME_PASSWORD_AUTH_VERSION);
            out.writeByte(0);
            
        } catch (IOException ex) {
            ex.printStackTrace(); // TODO
        }
    }
}
