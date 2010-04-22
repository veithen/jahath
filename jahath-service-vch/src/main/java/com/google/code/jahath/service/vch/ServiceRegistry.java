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
package com.google.code.jahath.service.vch;

import java.util.HashMap;
import java.util.Map;

import com.google.code.jahath.common.connection.Service;

class ServiceRegistry {
    private final Map<String,Service> services = new HashMap<String,Service>();
    
    public synchronized Service getService(String serviceName) {
        return services.get(serviceName);
    }
    
    public synchronized void registerService(String name, Service service) {
        if (services.containsKey(name)) {
            throw new IllegalArgumentException("Service '" + name + "' already registered");
        }
        services.put(name, service);
    }
}
