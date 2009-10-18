/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.jahath.client;

public class Tunnel {
    private final JahathClient client;
    private final String remoteHost;
    private final int remotePort;
    private Acceptor acceptor;
    
    Tunnel(JahathClient client, String remoteHost, int remotePort) {
        this.client = client;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    void setAcceptor(Acceptor acceptor) {
        this.acceptor = acceptor;
    }

    public JahathClient getClient() {
        return client;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void stop() {
        acceptor.stop();
    }
}
