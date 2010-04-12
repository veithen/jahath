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
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

public abstract class ConfigurationCommand implements Command {
	private final ServiceTracker serviceTracker;
	
	public ConfigurationCommand(BundleContext bundleContext) {
		serviceTracker = new ServiceTracker(bundleContext, ConfigurationAdmin.class.getName(), null);
		serviceTracker.open();
	}
	
	public final void execute(String commandLine, PrintStream out, PrintStream err) {
		ConfigurationAdmin configurationAdmin = (ConfigurationAdmin)serviceTracker.getService();
		if (configurationAdmin == null) {
			err.println("The configuration admin service is unavailable");
		} else {
			execute(configurationAdmin, new CommandLineParser(commandLine), out, err);
		}
	}
	
	protected abstract void execute(ConfigurationAdmin configurationAdmin, CommandLineParser commandLine, PrintStream out, PrintStream err);
}
