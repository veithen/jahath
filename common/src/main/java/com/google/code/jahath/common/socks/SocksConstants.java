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
    private SocksConstants() {}
    
    public static final byte SOCKS_VERSION = 5;
    
    public static final byte AUTH_NONE = 0;
    public static final byte AUTH_GSSAPI = 1;
    public static final byte AUTH_USERNAME_PASSWORD = 2;
    
    public static final byte USERNAME_PASSWORD_AUTH_VERSION = 1;
}
