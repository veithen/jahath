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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.code.jahath.AbstractConnection;
import com.jcraft.jsch.Channel;

public class ChannelConnection extends AbstractConnection {
    private final Channel channel;
    
    public ChannelConnection(Channel channel) {
        this.channel = channel;
    }

    public InputStream getInputStream() throws IOException {
        return channel.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return channel.getOutputStream();
    }

    @Override
    protected void doClose() throws IOException {
        channel.disconnect();
    }
}
