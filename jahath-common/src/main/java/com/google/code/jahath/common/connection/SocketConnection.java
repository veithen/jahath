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
package com.google.code.jahath.common.connection;

import java.io.IOException;
import java.net.Socket;

import com.googlecode.sisme.stream.AbstractConnection;
import com.googlecode.sisme.stream.Connection;
import com.googlecode.sisme.stream.InputStreamAdapter;
import com.googlecode.sisme.stream.OutputStreamAdapter;
import com.googlecode.sisme.stream.StreamSink;
import com.googlecode.sisme.stream.StreamSource;

/**
 * Adapts a {@link Socket} to the {@link Connection} interface.
 * 
 * @author Andreas Veithen
 */
public class SocketConnection extends AbstractConnection {
    private final Socket socket;

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }

    public StreamSource getStreamSource() throws IOException {
        return new InputStreamAdapter(new SocketInputStream(this, socket.getInputStream()));
    }

    public StreamSink getStreamSink() throws IOException {
        return new OutputStreamAdapter(socket.getOutputStream());
    }

    @Override
    protected void doClose() throws IOException {
        socket.close();
    }
}
