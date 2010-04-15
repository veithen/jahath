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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

public abstract class SimpleManagedServiceFactory implements ManagedServiceFactory {
    public class Instance {
        private final List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();
        private final List<DeletionListener> deletionListeners = new ArrayList<DeletionListener>();
        
        public void registerService(String clazz, Object service,
                Dictionary properties) {
            serviceRegistrations.add(bundleContext.registerService(clazz, service, properties));
        }
        
        public void addDeletionListener(DeletionListener listener) {
            deletionListeners.add(listener);
        }
        
        void deleted() {
            for (ServiceRegistration registration : serviceRegistrations) {
                registration.unregister();
            }
            for (DeletionListener listener : deletionListeners) {
                listener.deleted();
            }
        }
    }
    
    protected final BundleContext bundleContext;
    private final Map<String,Instance> instances = Collections.synchronizedMap(new HashMap<String,Instance>());
    
    public SimpleManagedServiceFactory(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public final void updated(String pid, Dictionary properties) throws ConfigurationException {
        Instance instance = instances.remove(pid);
        if (instance != null) {
            instance.deleted();
        }
        instance = new Instance();
        configure(instance, properties);
        instances.put(pid, instance);
    }

    protected abstract void configure(Instance instance, Dictionary properties) throws ConfigurationException;
    
    public final void deleted(String pid) {
        instances.remove(pid).deleted();
    }
}
