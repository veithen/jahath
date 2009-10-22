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
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class SocksDataOutputStream extends DataOutputStream {
    public SocksDataOutputStream(OutputStream out) {
        super(out);
    }
    
    public void writeASCII(String ascii) throws IOException {
        writeByte(ascii.length());
        write(ascii.getBytes("ascii"));
    }
    
    public void writeAddress(InetAddress address) throws IOException {
        // TODO: unfortunately there seems to be no reliable way to tell if the InetAddress has been created from an IP address or a host name
        byte[] addr = address.getAddress();
        if (addr.length == 4) {
            writeByte(SocksConstants.ADDRESS_IPV4);
        } else {
            writeByte(SocksConstants.ADDRESS_IPV6);
        }
        write(addr);
    }
    
    public void writeSocketAddress(InetSocketAddress socketAddress) throws IOException {
        writeAddress(socketAddress.getAddress());
        writeShort(socketAddress.getPort());
    }
}
