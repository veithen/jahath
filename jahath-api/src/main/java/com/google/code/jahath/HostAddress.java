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

public abstract class HostAddress {

    // TODO: detailed specs
    @Override
    public abstract String toString();
    
    public static HostAddress parse(String s) throws AddressParseException {
        int address = 0;
        int b = 0;
        int j = 0;
        boolean expectDigit = true;
        int i = 0;
        int c;
        do {
            c = i == s.length() ? -1 : s.charAt(i);
            if (c == '.' || c == -1) {
                if (expectDigit) {
                    return parseDnsAddress(s);
                } else {
                    address = (address << 8) | b;
                    j++;
                    b = 0;
                    expectDigit = c != -1;
                }
            } else if ('0' <= c && c <= '9') {
                b = 10*b + c - '0';
                expectDigit = false;
            } else {
                return parseDnsAddress(s);
            }
            i++;
        } while (c != -1);
        if (j == 4 && !expectDigit) {
            return new IPv4Address(address);
        } else {
            return parseDnsAddress(s);
        }
    }
    
    private static DnsAddress parseDnsAddress(String s) throws AddressParseException {
        // TODO: validate the address!
        return new DnsAddress(s);
    }
}
