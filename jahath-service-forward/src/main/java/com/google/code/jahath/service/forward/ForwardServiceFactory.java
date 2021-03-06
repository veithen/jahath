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

import java.util.Dictionary;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

import com.google.code.jahath.common.connection.Service;
import com.google.code.jahath.common.osgi.DeletionListener;
import com.google.code.jahath.common.osgi.SimpleManagedServiceFactory;

public class ForwardServiceFactory extends SimpleManagedServiceFactory {
    public ForwardServiceFactory(BundleContext bundleContext) {
        super(bundleContext);
    }

    @Override
    protected void configure(Instance instance, Dictionary properties) throws ConfigurationException {
        String name = (String)properties.get("name");
        String endpoint = (String)properties.get("endpoint");
        Properties serviceProps = new Properties();
        serviceProps.put("name", name);
        final ForwardService service = new ForwardService(bundleContext, endpoint);
        instance.registerService(Service.class.getName(), service, serviceProps);
        instance.addDeletionListener(new DeletionListener() {
            public void deleted() {
                service.destroy();
            }
        });
    }
}
