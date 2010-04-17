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
package com.google.code.jahath.common.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

public class NamedServiceProxy<T> {
    private final Class<T> serviceClass;
    private final String name;
    private final ServiceTracker tracker;

    public NamedServiceProxy(BundleContext bundleContext, Class<T> serviceClass, String name) {
        this.serviceClass = serviceClass;
        this.name = name;
        try {
            tracker = new ServiceTracker(bundleContext,
                    bundleContext.createFilter("(&(objectClass=" + serviceClass.getName() + ")(name=" + name + "))"), null);
            tracker.open();
        } catch (InvalidSyntaxException ex) {
            throw new Error(ex); // Should never get here
        }
    }
    
    protected T getTarget() {
        Object target = tracker.getService();
        if (target == null) {
            throw new ServiceUnavailableException(serviceClass.getSimpleName() + " " + name + " not available");
        }
        return serviceClass.cast(target);
    }
    
    public void release() {
        tracker.close();
    }
}
