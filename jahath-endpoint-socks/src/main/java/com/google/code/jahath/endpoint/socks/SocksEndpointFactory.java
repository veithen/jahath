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
package com.google.code.jahath.endpoint.socks;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

import com.google.code.jahath.common.connection.ConnectionHandler;

public class SocksEndpointFactory implements ManagedServiceFactory {
    private final BundleContext bundleContext;
    private final Map<String,SocksConnectionHandler> services = new HashMap<String,SocksConnectionHandler>();
    
    public SocksEndpointFactory(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void deleted(String pid) {
        // TODO Auto-generated method stub
        
    }

    public void updated(String pid, Dictionary properties) throws ConfigurationException {
        deleted(pid);
        String name = (String)properties.get("name");
        Properties serviceProps = new Properties();
        serviceProps.setProperty("name", name);
        SocksConnectionHandler endpoint = new SocksConnectionHandler(null /* TODO */);
        bundleContext.registerService(ConnectionHandler.class.getName(), endpoint, serviceProps);
        services.put(pid, endpoint);
    }
}
