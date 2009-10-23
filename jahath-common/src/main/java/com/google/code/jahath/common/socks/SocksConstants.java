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

public class SocksConstants {
    private static final String[] authProtocolDescriptions = { "None", "GSSAPI", "Username/Password" };
    
    private SocksConstants() {}
    
    public static final byte SOCKS_VERSION = 5;
    
    public static final byte AUTH_NONE = 0;
    public static final byte AUTH_GSSAPI = 1;
    public static final byte AUTH_USERNAME_PASSWORD = 2;
    
    public static final byte USERNAME_PASSWORD_AUTH_VERSION = 1;

    public static final byte REQUEST_TCP_CONNECTION = 1;
    public static final byte REQUEST_TCP_PORT_BINDING = 2;
    public static final byte REQUEST_UDP = 3;
    
    public static final byte ADDRESS_IPV4 = 1;
    public static final byte ADDRESS_DNS = 3;
    public static final byte ADDRESS_IPV6 = 4;
    
    public static final byte STATUS_OK = 0; // request granted
    public static final byte STATUS_GENERAL_FAILURE = 1;
    public static final byte STATUS_CONNECTION_NOT_ALLOWED = 2; // connection not allowed by ruleset
    public static final byte STATUS_NETWORK_UNREACHABLE = 3;
    public static final byte STATUS_HOST_UNREACHABLE = 4;
    public static final byte STATUS_CONNECTION_REFUSED = 5; // connection refused by destination host
    public static final byte STATUS_TTL_EXPIRED = 6;
    public static final byte STATUS_PROTOCOL_ERROR = 7; // command not supported / protocol error
    public static final byte STATUS_ADDRESS_TYPE_NOT_SUPPORTED = 8;
    
    public static String getAuthProtocolDescription(byte protocol) {
        return protocol < 0 || protocol > 2 ? "unknown" : authProtocolDescriptions[protocol];
    }
}
