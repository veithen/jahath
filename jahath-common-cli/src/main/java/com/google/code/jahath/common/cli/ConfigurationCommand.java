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

import java.io.IOException;
import java.io.PrintStream;
import java.util.Dictionary;
import java.util.Properties;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class ConfigurationCommand implements Command {
    private final String name;
    private final String shortDescription;
    private final String factoryPid;
    private final CommandParser<Dictionary> parser;
    // TODO: we don't really need trackers here; just do a lookup when executing the command!
    private final ServiceTracker configurationAdminTracker;
    private final ServiceTracker managedServiceTracker;
    
    public ConfigurationCommand(BundleContext bundleContext, String name, String shortDescription, String factoryPid, CommandParser<Dictionary> parser) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.factoryPid = factoryPid;
        this.parser = parser;
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
    
    public final String getName() {
        return name;
    }

    public final String getShortDescription() {
        return shortDescription;
    }

    public String getUsage() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(name);
        buffer.append(" [add ");
        parser.formatUsage(buffer);
        buffer.append(" | del <id>]");
        return buffer.toString();
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
        // TODO: not sure if we really need a Configurator class
        Configurator configurator = new Configurator(configurationAdmin, factoryPid, managedServiceRef.getBundle().getLocation());
        CommandLine p = new CommandLine(commandLine);
        try {
            p.consume();
            String subcommand = p.consume();
            if (subcommand == null) {
                out.println("Invalid command: expected 'add' or 'del'");
            } else if (subcommand.equals("add")) {
                try {
                    Properties props = new Properties();
                    parser.parse(p, props);
                    configurator.createConfiguration().update(props);
                } catch (ParseException ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (subcommand.equals("del")) {
                // TODO
            }
        } catch (IOException ex) {
            ex.printStackTrace(err);
            return;
        }
    }
}
