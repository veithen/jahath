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

import java.util.Dictionary;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.common.connection.Service;
import com.google.code.jahath.common.http.server.HttpService;
import com.google.code.jahath.common.osgi.DeletionListener;
import com.google.code.jahath.common.osgi.SimpleManagedServiceFactory;

public class VCHServiceFactory extends SimpleManagedServiceFactory {
    public VCHServiceFactory(BundleContext bundleContext) {
        super(bundleContext);
    }

    @Override
    protected void configure(Instance instance, Dictionary properties) throws ConfigurationException {
        String name = (String)properties.get("name");
        String[] services = (String[])properties.get("services");
        final ServiceRegistry serviceRegistry = new ServiceRegistry();
        StringBuilder filterExpr = new StringBuilder();
        filterExpr.append("(&(objectClass=");
        filterExpr.append(Service.class.getName());
        filterExpr.append(")(|");
        for (Object service : services) {
            filterExpr.append("(name=");
            filterExpr.append(service);
            filterExpr.append(")");
        }
        filterExpr.append("))");
        Filter filter;
        try {
            filter = bundleContext.createFilter(filterExpr.toString());
        } catch (InvalidSyntaxException ex) {
            throw new Error(ex);
        }
        final ServiceTracker serviceTracker = new ServiceTracker(bundleContext, filter, null) {
            @Override
            public Object addingService(ServiceReference reference) {
                Service service = (Service)super.addingService(reference);
                serviceRegistry.registerService((String)reference.getProperty("name"), service);
                return service;
            }

            @Override
            public void removedService(ServiceReference reference, Object service) {
                super.removedService(reference, service);
                serviceRegistry.unregisterService((String)reference.getProperty("name"));
            }
        };
        serviceTracker.open();
        Properties serviceProps = new Properties();
        serviceProps.put("name", name);
        instance.registerService(Service.class.getName(), new HttpService(new HttpRequestHandlerImpl(serviceRegistry)), serviceProps);
        instance.addDeletionListener(new DeletionListener() {
            public void deleted() {
                serviceTracker.close();
            }
        });
    }
}
