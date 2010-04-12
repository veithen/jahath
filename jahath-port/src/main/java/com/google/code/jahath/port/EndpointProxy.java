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

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.Connection;
import com.google.code.jahath.common.connection.ConnectionHandler;
import com.google.code.jahath.common.container.ExecutionEnvironment;

public class EndpointProxy implements ConnectionHandler {
    private final String name;
    private final ServiceTracker tracker;
    
    public EndpointProxy(BundleContext bundleContext, String name) {
        this.name = name;
        try {
            tracker = new ServiceTracker(bundleContext,
                    bundleContext.createFilter("(&(objectClass=" + ConnectionHandler.class.getName() + ")(name=" + name + "))"), null);
            tracker.open(); // TODO: do we need to shut this down somewhere???
        } catch (InvalidSyntaxException ex) {
            throw new Error(ex); // Should never get here
        }
    }

    public void handle(ExecutionEnvironment env, Connection connection) {
        ConnectionHandler target = (ConnectionHandler)tracker.getService();
        if (target == null) {
            throw new IllegalStateException("Endpoint " + name + " not available");
        }
        target.handle(env, connection);
    }
}
