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
package com.google.code.jahath.tcp;

import com.google.code.jahath.AddressParseException;
import com.google.code.jahath.HostAddress;

// TODO: implement equals and hashCode
public class SocketAddress {
    private final HostAddress host;
    private final int port;
    
    public SocketAddress(HostAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public static SocketAddress parse(String s) throws AddressParseException {
        return parse(s, -1);
    }
    
    public static SocketAddress parse(String s, int defaultPort) throws AddressParseException {
        int idx = s.lastIndexOf(':');
        if (idx == -1) {
            if (defaultPort == -1) {
                throw new AddressParseException("Socket address doesn't have a port number");
            } else {
                return new SocketAddress(HostAddress.parse(s), defaultPort);
            }
        } else {
            HostAddress host = HostAddress.parse(s.substring(0, idx));
            int port;
            try {
                port = Integer.parseInt(s.substring(idx+1));
            } catch (NumberFormatException ex) {
                throw new AddressParseException("Non numeric port number");
            }
            // TODO: check port number range
            return new SocketAddress(host, port);
        }
    }
    
    public HostAddress getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SocketAddress) {
            SocketAddress other = (SocketAddress)obj;
            return other.host.equals(host) && other.port == port;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return host.toString() + ":" + port;
    }
}
