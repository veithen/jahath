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
package com.google.code.jahath.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import com.google.code.jahath.common.connection.Connection;

class ConnectionImpl implements Connection {
    static class SessionInputStream extends InputStream {
        InputStream parent;

        private void awaitParent() throws InterruptedIOException {
            while (parent == null) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException();
                }
            }
        }
        
        @Override
        public synchronized int read() throws IOException {
            while (true) {
                awaitParent();
                int b = parent.read();
                if (b == -1) {
                    parent = null;
                    notifyAll();
                } else {
                    return b;
                }
            }
        }

        @Override
        public synchronized int read(byte[] b, int off, int len) throws IOException {
            while (true) {
                awaitParent();
                int c = parent.read(b, off, len);
                if (c == -1) {
                    parent = null;
                    notifyAll();
                } else {
                    return c;
                }
            }
        }
    }
    
    static class SessionOutputStream extends OutputStream {
        OutputStream parent;

        private void awaitParent() throws InterruptedIOException {
            while (parent == null) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException();
                }
            }
        }
        
        @Override
        public synchronized void write(byte[] b, int off, int len) throws IOException {
            awaitParent();
            parent.write(b, off, len);
        }

        @Override
        public synchronized void write(int b) throws IOException {
            awaitParent();
            parent.write(b);
        }

        @Override
        public synchronized void flush() throws IOException {
            parent = null;
            notifyAll();
        }
    }
    
    private final String id;
    private final SessionInputStream sessionInputStream = new SessionInputStream();
    private final SessionOutputStream sessionOutputStream = new SessionOutputStream();
    
    ConnectionImpl(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    void consume(InputStream in) throws InterruptedException {
        synchronized (sessionInputStream) {
            while (sessionInputStream.parent != null) {
                sessionInputStream.wait();
            }
            sessionInputStream.parent = in;
            sessionInputStream.notifyAll();
            while (sessionInputStream.parent != null) {
                sessionInputStream.wait();
            }
        }
    }
    
    void produce(OutputStream out) throws InterruptedException {
        synchronized (sessionOutputStream) {
            while (sessionOutputStream.parent != null) {
                sessionOutputStream.wait();
            }
            sessionOutputStream.parent = out;
            sessionOutputStream.notifyAll();
            while (sessionOutputStream.parent != null) {
                sessionOutputStream.wait();
            }
        }
    }
    
    public InputStream getInputStream() {
        return sessionInputStream;
    }
    
    public OutputStream getOutputStream() {
        return sessionOutputStream;
    }
}
