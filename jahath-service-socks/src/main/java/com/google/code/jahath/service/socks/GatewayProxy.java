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
package com.google.code.jahath.service.socks;

import java.io.IOException;

import org.osgi.framework.BundleContext;

import com.google.code.jahath.common.osgi.NamedServiceProxy;
import com.google.code.jahath.tcp.Gateway;
import com.google.code.jahath.tcp.SocketAddress;
import com.googlecode.sisme.stream.Connection;

// TODO: eliminate this
public class GatewayProxy extends NamedServiceProxy<Gateway> implements Gateway {
    public GatewayProxy(BundleContext bundleContext, String name) {
        super(bundleContext, Gateway.class, name);
    }

    public Connection connect(SocketAddress socketAddress) throws IOException {
        return getTarget().connect(socketAddress);
    }
}
