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
package com.google.code.jahath.resolver.impl;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.code.jahath.DnsAddress;
import com.google.code.jahath.IPAddress;
import com.google.code.jahath.resolver.Resolver;
import com.google.code.jahath.resolver.ResolverPlugin;

public class ResolverImpl implements Resolver {
    private final ServiceTracker tracker;
    
    public ResolverImpl(BundleContext bundleContext) {
        tracker = new ServiceTracker(bundleContext, ResolverPlugin.class.getName(), null);
        tracker.open();
    }

    public IPAddress resolve(DnsAddress address) {
        for (Object service : tracker.getServices()) {
            IPAddress result = ((ResolverPlugin)service).resolve(address);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
