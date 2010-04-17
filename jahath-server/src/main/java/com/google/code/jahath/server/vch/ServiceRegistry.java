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
package com.google.code.jahath.server.vch;

import java.util.HashMap;
import java.util.Map;

import com.google.code.jahath.common.connection.Endpoint;

class ServiceRegistry {
    private final Map<String,Endpoint> services = new HashMap<String,Endpoint>();
    
    public synchronized Endpoint getEndpoint(String serviceName) {
        return services.get(serviceName);
    }
    
    public synchronized void registerService(String name, Endpoint endpoint) {
        if (services.containsKey(name)) {
            throw new IllegalArgumentException("Service '" + name + "' already registered");
        }
        services.put(name, endpoint);
    }
}
