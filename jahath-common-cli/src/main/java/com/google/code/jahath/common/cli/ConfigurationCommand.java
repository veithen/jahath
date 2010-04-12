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
package com.google.code.jahath.common.cli;

import java.io.PrintStream;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public abstract class ConfigurationCommand implements Command {
    private final String factoryPid;
    private final ServiceTracker configurationAdminTracker;
    private final ServiceTracker managedServiceTracker;
    
    public ConfigurationCommand(BundleContext bundleContext, String factoryPid) {
        this.factoryPid = factoryPid;
        configurationAdminTracker = new ServiceTracker(bundleContext, ConfigurationAdmin.class.getName(), null);
        try {
            managedServiceTracker = new ServiceTracker(bundleContext,
                    bundleContext.createFilter("(&(objectClass=org.osgi.service.cm.ManagedServiceFactory)(service.pid=" + factoryPid + "))"), null);
        } catch (InvalidSyntaxException ex) {
            throw new Error(ex); // We should never get here
        }
        configurationAdminTracker.open();
        managedServiceTracker.open();
    }
    
    public final void execute(String commandLine, PrintStream out, PrintStream err) {
        ConfigurationAdmin configurationAdmin = (ConfigurationAdmin)configurationAdminTracker.getService();
        if (configurationAdmin == null) {
            err.println("The configuration admin service is unavailable");
            return;
        }
        ServiceReference managedServiceRef = managedServiceTracker.getServiceReference();
        if (managedServiceRef == null) {
            err.println("The managed service with service.pid " + factoryPid + " is unavailable");
            return;
        }
        execute(new Configurator(configurationAdmin, factoryPid, managedServiceRef.getBundle().getLocation()),
                new CommandLineParser(commandLine), out, err);
    }
    
    protected abstract void execute(Configurator configurator, CommandLineParser commandLine, PrintStream out, PrintStream err);
}
