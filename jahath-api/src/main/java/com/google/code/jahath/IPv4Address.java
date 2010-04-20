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
    public IPv4Address(byte[] address) {
        super(address);
        if (address == null || address.length != 4) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return (address[0] & 0xFF) + "." + (address[1] & 0xFF) + "." + (address[2] & 0xFF) + "." + (address[3] & 0xFF);
    }

    @Override
    public int hashCode() {
        return (address[0] & 0xFF) << 24 | (address[1] & 0xFF) << 16 | (address[2] & 0xFF) << 8 | (address[3] & 0xFF);
    }
}
