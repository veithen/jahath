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
package com.google.code.jahath.gateway.socks.cli;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.code.jahath.common.cli.Argument;
import com.google.code.jahath.common.cli.ConfigurationCommand;
import com.google.code.jahath.common.cli.Sequence;

public class Activator implements BundleActivator {
    public void start(BundleContext context) throws Exception {
        Sequence sequence = new Sequence();
        sequence.add(new Argument("name"));
        sequence.add(new Argument("endpoint"));
        context.registerService(Command.class.getName(), new ConfigurationCommand(context,
                "socksgw", "Manage SOCKS gateways", "gateway-socks", sequence), null);
    }

    public void stop(BundleContext context) throws Exception {
    }
}
