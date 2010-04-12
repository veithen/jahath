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
package com.google.code.jahath.port.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;

import com.google.code.jahath.common.cli.CommandLineParser;
import com.google.code.jahath.common.cli.ConfigurationCommand;
import com.google.code.jahath.common.cli.Configurator;

public class CommandImpl extends ConfigurationCommand {
    public CommandImpl(BundleContext bundleContext) {
        super(bundleContext, "port");
    }

    public String getName() {
        return "port";
    }

    public String getShortDescription() {
        return "Manage ports";
    }

    public String getUsage() {
        return "port [add <port> <endpoint> | del <port>]";
    }
    
    @Override
	protected void execute(Configurator configurator, CommandLineParser p, PrintStream out, PrintStream err) {
        try {
            p.consume();
            String subcommand = p.consume();
            if (subcommand == null) {
                out.println("Invalid command: expected 'add' or 'del'");
            } else if (subcommand.equals("add")) {
                String port = p.consume();
                String endpoint = p.consume();
                if (port == null || endpoint == null) {
                    out.println("Invalid command");
                } else {
                    Configuration config = configurator.createConfiguration();
                    Dictionary props = new Hashtable();
                    props.put("port", Integer.valueOf(port));
                    props.put("endpoint", endpoint);
                    config.update(props);
                }
            } else if (subcommand.equals("del")) {
                
            }
        } catch (IOException ex) {
            ex.printStackTrace(err);
            return;
        }
    }
}
