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
package com.google.code.jahath.endpoint.socks.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.felix.shell.Command;
import org.osgi.framework.Bundle;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.google.code.jahath.common.cli.CommandLineParser;

public class CommandImpl implements Command {
    private final ConfigurationAdmin configurationAdmin;
    private final Bundle bundle;
    
    public CommandImpl(ConfigurationAdmin configurationAdmin, Bundle bundle) {
        this.configurationAdmin = configurationAdmin;
        this.bundle = bundle;
    }

    public String getName() {
        return "socksep";
    }

    public String getShortDescription() {
        return "Manipulate SOCKS endpoints";
    }

    public String getUsage() {
        return "socksep [add <name> <gateway> | del <name>]";
    }
    
    public void execute(String commandLine, PrintStream out, PrintStream err) {
        try {
            CommandLineParser p = new CommandLineParser(commandLine);
            p.consume();
            String subcommand = p.consume();
            if (subcommand == null) {
                out.println("Invalid command: expected 'add' or 'del'");
            } else if (subcommand.equals("add")) {
                String name = p.consume();
                String gateway = p.consume();
                if (name == null || gateway == null) {
                    out.println("Invalid command");
                } else {
                    Configuration config = configurationAdmin.createFactoryConfiguration("endpoint-socks", bundle.getLocation());
                    Properties props = new Properties();
                    props.setProperty("name", name);
                    props.setProperty("gateway", gateway);
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
