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
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

import com.google.code.jahath.common.connection.Endpoint;
import com.google.code.jahath.common.osgi.DeletionListener;
import com.google.code.jahath.common.osgi.GatewayProxy;
import com.google.code.jahath.common.osgi.SimpleManagedServiceFactory;

public class SocksEndpointFactory extends SimpleManagedServiceFactory {
    public SocksEndpointFactory(BundleContext bundleContext) {
        super(bundleContext);
    }

    @Override
    protected void configure(Instance instance, Dictionary properties) throws ConfigurationException {
        String name = (String)properties.get("name");
        String gateway = (String)properties.get("gateway");
        final GatewayProxy gatewayProxy = new GatewayProxy(bundleContext, gateway);
        instance.addDeletionListener(new DeletionListener() {
            public void deleted() {
                gatewayProxy.release();
            }
        });
        SocksEndpoint endpoint = new SocksEndpoint(gatewayProxy);
        Properties serviceProps = new Properties();
        serviceProps.put("name", name);
        instance.registerService(Endpoint.class.getName(), endpoint, serviceProps);
    }
}
