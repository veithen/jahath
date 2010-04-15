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
package com.google.code.jahath.port;

import java.io.IOException;
import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

import com.google.code.jahath.common.osgi.DeletionListener;
import com.google.code.jahath.common.osgi.SimpleManagedServiceFactory;
import com.google.code.jahath.common.server.Server;

public class PortFactory extends SimpleManagedServiceFactory {
    public PortFactory(BundleContext bundleContext) {
        super(bundleContext);
    }
    
    @Override
    protected void configure(Instance instance, Dictionary properties) throws ConfigurationException {
        int port = (Integer)properties.get("port");
        String endpoint = (String)properties.get("endpoint");
        
        try {
            final Server server = new Server(port, new EndpointProxy(bundleContext, endpoint));
            instance.addDeletionListener(new DeletionListener() {
                public void deleted() {
                    try {
                        server.stop();
                    } catch (InterruptedException ex) {
                        Thread.interrupted();
                    }
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex); // TODO
        }
        
    }
}
