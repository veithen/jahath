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
package com.google.code.jahath.tcp.impl;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedServiceFactory;

import com.google.code.jahath.tcp.Gateway;

public class Activator implements BundleActivator {
    public void start(BundleContext context) throws Exception {
        registerPortFactory(context);
        registerTcpEndpointFactory(context);
        registerDirectGateway(context);
    }
    
    private void registerPortFactory(BundleContext context) {
        Properties props = new Properties();
        props.setProperty("service.pid", "port");
        context.registerService(ManagedServiceFactory.class.getName(), new PortFactory(context), props);
    }
    
    private void registerTcpEndpointFactory(BundleContext context) {
        Properties props = new Properties();
        props.setProperty("service.pid", "endpoint-tcp");
        context.registerService(ManagedServiceFactory.class.getName(), new TcpEndpointFactory(context), props);
    }
    
    private void registerDirectGateway(BundleContext context) {
        Properties props = new Properties();
        props.put("name", "direct");
        context.registerService(Gateway.class.getName(), new DirectGateway(context), props);
    }

    public void stop(BundleContext context) throws Exception {
    }
}
