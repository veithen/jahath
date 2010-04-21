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
package com.google.code.jahath.common.socks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.code.jahath.DnsAddress;
import com.google.code.jahath.HostAddress;
import com.google.code.jahath.IPv4Address;
import com.google.code.jahath.IPv6Address;
import com.google.code.jahath.SocketAddress;

public class SocksDataOutputStream extends DataOutputStream {
    public SocksDataOutputStream(OutputStream out) {
        super(out);
    }
    
    public void writeASCII(String ascii) throws IOException {
        writeByte(ascii.length());
        write(ascii.getBytes("ascii"));
    }
    
    public void writeAddress(HostAddress address) throws IOException {
        if (address instanceof DnsAddress) {
            writeByte(SocksConstants.ADDRESS_DNS);
            writeASCII(((DnsAddress)address).getName());
        } else if (address instanceof IPv4Address) {
            writeByte(SocksConstants.ADDRESS_IPV4);
            write(((IPv4Address)address).getAddress());
        } else if (address instanceof IPv6Address) {
            writeByte(SocksConstants.ADDRESS_IPV6);
            write(((IPv6Address)address).getAddress());
        } else {
            throw new UnsupportedOperationException("Unsupported address type: " + address.getClass().getName());
        }
    }
    
    public void writeSocketAddress(SocketAddress socketAddress) throws IOException {
        writeAddress(socketAddress.getHost());
        writeShort(socketAddress.getPort());
    }
}
