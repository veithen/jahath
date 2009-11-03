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
package com.google.code.jahath.client.tunnel;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.google.code.jahath.client.VCHClient;
import com.google.code.jahath.common.server.Server;

public class Tunnel extends Server {
    public Tunnel(int port, VCHClient client, InetSocketAddress target) throws IOException {
        super(port, new TunnelConnectionHandler(client, target));
    }
}
