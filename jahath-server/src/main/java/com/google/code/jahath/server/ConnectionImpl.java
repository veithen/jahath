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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.LogUtil;
import com.google.code.jahath.common.connection.Connection;

class ConnectionImpl implements Connection {
    class SessionInputStream extends InputStream {
        InputStream parent;
        private boolean closed;

        private void awaitParent() throws InterruptedIOException {
            while (parent == null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(id +": Waiting for new input stream");
                }
                try {
                    wait();
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException();
                }
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine(id +": Got new input stream");
            }
        }
        
        private void consumed() {
            parent = null;
            if (log.isLoggable(Level.FINE)) {
                log.fine(id +": Input stream consumed");
            }
            notifyAll();
        }
        
        @Override
        public synchronized int read() throws IOException {
            while (true) {
                awaitParent();
                int b = parent.read();
                if (b == -1) {
                    consumed();
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
                    consumed();
                } else {
                    return c;
                }
            }
        }

        @Override
        public synchronized void close() throws IOException {
            closed = true;
        }
    }
    
    class SessionOutputStream extends OutputStream {
        OutputStream parent;

        private void awaitParent() throws InterruptedIOException {
            while (parent == null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(id +": Waiting for new output stream");
                }
                try {
                    wait();
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException();
                }
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine(id +": Got new output stream");
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
            if (log.isLoggable(Level.FINE)) {
                log.fine(id +": Output stream flushed");
            }
            notifyAll();
        }
    }
    
    static final Logger log = Logger.getLogger(ConnectionImpl.class.getName());
    
    final String id;
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
                if (log.isLoggable(Level.FINE)) {
                    log.fine(id + ": Waiting for connection to be ready to accept new input stream");
                }
                sessionInputStream.wait();
            }
            sessionInputStream.parent = in;
            if (log.isLoggable(Level.FINE)) {
                log.fine(id + ": Input stream connected");
            }
            sessionInputStream.notifyAll();
            while (sessionInputStream.parent != null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(id + ": Waiting for connection to disconnect input stream");
                }
                sessionInputStream.wait();
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine(id + ": Input stream disconnected");
            }
        }
    }
    
    void produce(OutputStream out) throws InterruptedException {
        synchronized (sessionOutputStream) {
            while (sessionOutputStream.parent != null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(id + ": Waiting for connection to be ready to accept new output stream");
                }
                sessionOutputStream.wait();
            }
            sessionOutputStream.parent = out;
            if (log.isLoggable(Level.FINE)) {
                log.fine(id + ": Output stream connected");
            }
            sessionOutputStream.notifyAll();
            while (sessionOutputStream.parent != null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(id + ": Waiting for connection to disconnect output stream");
                }
                sessionOutputStream.wait();
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine(id + ": Output stream disconnected");
            }
        }
    }
    
    public InputStream getInputStream() {
        return LogUtil.log(sessionInputStream, log, Level.FINER, "in");
    }
    
    public OutputStream getOutputStream() {
        return LogUtil.log(sessionOutputStream, log, Level.FINER, "out");
    }

    public void close() throws IOException {
        // TODO
    }
}
