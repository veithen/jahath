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
package com.google.code.jahath.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link InputStream} proxy that supports swapping of the target in a concurrent environment. The
 * standard scenario for this class is as follows:
 * <ul>
 * <li>The {@link SwappableInputStream} is consumed by code running in a given thread.
 * <li>The data to be consumed is provided by another thread (or group of threads) as chunks, all of
 * which are represented as individual {@link InputStream} instances.
 * </ul>
 * This class allows the data to be exchanged between the two threads in such a way that it appears
 * to the consumer as a single stream.
 * 
 * @author Andreas Veithen
 */
// TODO: isn't the synchronization code equivalent to usage of SynchronuousQueue?
public class SwappableInputStream extends InputStream {
    private static final Logger log = Logger.getLogger(SwappableInputStream.class.getName());
    
    private final String label;
    private InputStream parent;
    private boolean endOfStream;

    public SwappableInputStream(String label) {
        this.label = label;
    }

    public synchronized void swap(InputStream in) throws InterruptedException {
        // TODO: should we really allow cases where swap is called concurrently by two different threads?
        while (parent != null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(label + ": Waiting for connection to be ready to accept new input stream");
            }
            wait();
        }
        parent = in;
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + ": Input stream connected");
        }
        notifyAll();
        while (parent != null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(label + ": Waiting for connection to disconnect input stream");
            }
            wait();
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + ": Input stream disconnected");
        }
    }
    
    public synchronized void sendEndOfStream() {
        // TODO: implement the same behavior in swap and sendEndOfStream
        if (parent != null) {
            throw new IllegalStateException();
        }
        endOfStream = true;
        notifyAll();
    }
    
    private void awaitParent() throws InterruptedIOException {
        if (parent == null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(label + ": Waiting for new input stream");
            }
            while (parent == null && !endOfStream) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException();
                }
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine(label +": Got new input stream");
            }
        }
    }
    
    private void consumed() {
        parent = null;
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + ": Input stream consumed");
        }
        notifyAll();
    }
    
    @Override
    public synchronized int read() throws IOException {
        while (true) {
            awaitParent();
            if (endOfStream) {
                return -1;
            } else {
                int b = parent.read();
                if (b == -1) {
                    consumed();
                } else {
                    return b;
                }
            }
        }
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        while (true) {
            awaitParent();
            if (endOfStream) {
                return -1;
            } else {
                int c = parent.read(b, off, len);
                if (c == -1) {
                    consumed();
                } else {
                    return c;
                }
            }
        }
    }

    @Override
    public synchronized void close() throws IOException {
//        closed = true;
    }
}