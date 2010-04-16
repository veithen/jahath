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
package com.google.code.jahath.gateway.ssh.cli;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.code.jahath.common.cli.ConfigurationCommand;
import com.google.code.jahath.common.cli.IntegerArgument;
import com.google.code.jahath.common.cli.Sequence;
import com.google.code.jahath.common.cli.StringArgument;

public class Activator implements BundleActivator {
    public void start(BundleContext context) throws Exception {
        Sequence sequence = new Sequence();
        sequence.add(new StringArgument("name"));
        // TODO: see if we can use "host:port" syntax here
        sequence.add(new StringArgument("host"));
        sequence.add(new IntegerArgument("port"));
        sequence.add(new StringArgument("user"));
        sequence.add(new StringArgument("password"));
        context.registerService(Command.class.getName(), new ConfigurationCommand(context, "ssh", "Manipulate SSH gateways", "gateway-ssh", sequence), null);
    }

    public void stop(BundleContext context) throws Exception {
    }
}
