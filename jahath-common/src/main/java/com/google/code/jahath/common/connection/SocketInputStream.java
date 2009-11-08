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
import java.io.InputStream;

class SocketInputStream extends InputStream {
    private final SocketConnection connection;
    private final InputStream parent;

    public SocketInputStream(SocketConnection connection, InputStream parent) {
        this.connection = connection;
        this.parent = parent;
    }

    private boolean isClosedLocally() {
        return connection.getState() != Connection.State.OPEN;
    }
    
    @Override
    public int available() throws IOException {
        try {
            return parent.available();
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public void close() throws IOException {
        parent.close();
    }

    @Override
    public int read() throws IOException {
        try {
            return parent.read();
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            return parent.read(b, off, len);
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        try {
            return parent.read(b);
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return parent.skip(n);
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }
}
