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
package com.google.code.jahath.gateway.ssh;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;

import com.google.code.jahath.common.osgi.SimpleManagedServiceFactory;
import com.google.code.jahath.tcp.Gateway;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHGatewayFactory extends SimpleManagedServiceFactory {
    private final JSch jsch;
    
    public SSHGatewayFactory(BundleContext bundleContext, JSch jsch) {
        super(bundleContext);
        this.jsch = jsch;
    }

    @Override
    protected void configure(Instance instance, Dictionary properties) throws ConfigurationException {
        String name = (String)properties.get("name");
        String host = (String)properties.get("host");
        Integer port = (Integer)properties.get("port");
        String user = (String)properties.get("user");
        String password = (String)properties.get("password");
        // TODO: validate configuration
        Session session;
        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.connect(); // TODO: connect should be done asynchronously and we should support a retry mechanism
        } catch (JSchException ex) {
            throw new RuntimeException(ex); // TODO
        }
        Dictionary serviceProps = new Hashtable();
        serviceProps.put("name", name);
        instance.registerService(Gateway.class.getName(), new SSHGateway(session), serviceProps);
    }
}
