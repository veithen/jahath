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
package com.google.code.jahath.gateway.ssh;

import java.io.IOException;

import com.google.code.jahath.Connection;
import com.google.code.jahath.Gateway;
import com.google.code.jahath.SocketAddress;
import com.jcraft.jsch.ChannelDirectTCPIP;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHGateway implements Gateway {
    private final Session session;
    
    public SSHGateway(Session session) {
        this.session = session;
    }

    public Connection connect(SocketAddress socketAddress) throws IOException {
        try {
            ChannelDirectTCPIP channel = (ChannelDirectTCPIP)session.openChannel("direct-tcpip");
            channel.setHost(socketAddress.getHost().toString());
            channel.setPort(socketAddress.getPort());
    //        channel.setOrgIPAddress(socket.getInetAddress().getHostAddress());
    //        channel.setOrgPort(socket.getPort());
            channel.connect();
            return new ChannelConnection(channel);
        } catch (JSchException ex) {
            IOException ioEx = new IOException();
            ioEx.initCause(ex);
            throw ioEx;
        }
    }
}
