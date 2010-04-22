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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.code.jahath.DnsAddress;
import com.google.code.jahath.HostAddress;
import com.google.code.jahath.IPv4Address;
import com.google.code.jahath.IPv6Address;
import com.google.code.jahath.tcp.SocketAddress;

public class SocksDataInputStream extends DataInputStream {
    public SocksDataInputStream(InputStream in) {
        super(in);
    }
    
    public String readASCII() throws IOException {
        int len = readUnsignedByte();
        byte[] b = new byte[len];
        readFully(b);
        return new String(b, "ascii");
    }
    
    public HostAddress readAddress() throws IOException {
    	switch (readByte()) {
    		case SocksConstants.ADDRESS_IPV4: {
    			byte[] addr = new byte[4];
    			readFully(addr);
    			return new IPv4Address(addr);
    		}
    		case SocksConstants.ADDRESS_DNS:
    			return new DnsAddress(readASCII());
    		case SocksConstants.ADDRESS_IPV6: {
    			byte[] addr = new byte[16];
    			readFully(addr);
    			return new IPv6Address(addr);
    		}
    		default:
    			return null; // TODO
    	}
    }
    
    public SocketAddress readSocketAddress() throws IOException {
    	return new SocketAddress(readAddress(), readUnsignedShort());
    }
}
