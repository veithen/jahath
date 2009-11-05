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
package com.google.code.jahath.common.vch;

/**
 * Defines constants used by VC/H clients and servers.
 * 
 * @author Andreas Veithen
 */
public class VCHConstants {
    private VCHConstants() {}

    /**
     * Defines constants for well known service endpoint names.
     */
    public static class Services {
        private Services() {}
        
        /**
         * The name of the echo service endpoint.
         */
        public static final String ECHO = "echo";
        
        /**
         * The name of the SOCKS 5 service endpoint.
         */
        public static final String SOCKS = "socks";
    }
}
