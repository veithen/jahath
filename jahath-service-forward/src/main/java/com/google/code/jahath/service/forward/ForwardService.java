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
package com.google.code.jahath.service.forward;

import java.io.IOException;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.Connection;
import com.google.code.jahath.Endpoint;
import com.google.code.jahath.common.ConnectionRelay;
import com.google.code.jahath.common.connection.Service;
import com.google.code.jahath.common.container.ExecutionEnvironment;

public class ForwardService implements Service {
    private static final Logger log = Logger.getLogger(ForwardService.class.getName());
    
    private final ServiceTracker endpointTracker;
    
    public ForwardService(BundleContext bundleContext, String endpointName) {
        try {
            endpointTracker = new ServiceTracker(bundleContext,
                    bundleContext.createFilter("(&(objectClass=" + Endpoint.class.getName() + ")(name=" + endpointName + "))"), null);
            endpointTracker.open();
        } catch (InvalidSyntaxException ex) {
            throw new Error(ex);
        }
    }

    public void handle(ExecutionEnvironment env, Connection connection) {
        Endpoint endpoint = (Endpoint)endpointTracker.getService();
        if (endpoint == null) {
            log.severe("Endpoint not available");
        } else {
            try {
                // TODO: generate proper labels
                new ConnectionRelay(log, env, connection, "???", endpoint.connect(), "jahath-server").run();
            } catch (IOException ex) {
                // TODO
                ex.printStackTrace();
            }
        }
    }
    
    public void destroy() {
        endpointTracker.close();
    }
}
