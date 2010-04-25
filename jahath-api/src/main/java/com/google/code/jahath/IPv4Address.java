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
package com.google.code.jahath;

public class IPv4Address extends IPAddress {
    public static final IPv4Address LOOPBACK = new IPv4Address(new byte[] { 127, 0, 0, 1 });
    
    private final int address;
    
    public IPv4Address(int address) {
        this.address = address;
    }
    
    public IPv4Address(byte[] address) {
        if (address == null || address.length != 4) {
            throw new IllegalArgumentException();
        }
        this.address = (address[0] & 0xFF) << 24 | (address[1] & 0xFF) << 16 | (address[2] & 0xFF) << 8 | (address[3] & 0xFF);
    }
    
    public byte[] getAddress() {
        byte[] result = new byte[4];
        result[0] = (byte)(address >> 24);
        result[1] = (byte)(address >> 16);
        result[2] = (byte)(address >> 8);
        result[3] = (byte)address;
        return result;
    }

    @Override
    public String toString() {
        return ((address >> 24) & 0xFF) + "." + ((address >> 16) & 0xFF) + "." + ((address >> 8) & 0xFF) + "." + (address & 0xFF);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IPv4Address && ((IPv4Address)obj).address == address;
    }

    @Override
    public int hashCode() {
        return address;
    }
}
