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
package com.google.code.jahath.server.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link OutputStream} proxy that supports swapping of the target in a concurrent environment. The
 * standard scenario for this class is as follows:
 * <ul>
 * <li>Data is written to the {@link SwappableOutputStream} by code running in a given thread.
 * <li>The data is consumed by another thread (or group of threads) as chunks. For every chunk, the
 * consumer provides an individual {@link OutputStream} instance.
 * </ul>
 * This class allows the data to be exchanged between the two threads in a way that is completely
 * transparent to the producer.
 * <p>
 * The chunks are delimited by calls to {@link SwappableOutputStream#flush()}.
 * 
 * @author Andreas Veithen
 */
public class SwappableOutputStream extends OutputStream {
    private static final Logger log = Logger.getLogger(SwappableOutputStream.class.getName());
    
    private final String label;
    private OutputStream parent;

    public SwappableOutputStream(String label) {
        this.label = label;
    }

    public synchronized void swap(OutputStream out) throws InterruptedException {
        while (parent != null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(label + ": Waiting for connection to be ready to accept new output stream");
            }
            wait();
        }
        parent = out;
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + ": Output stream connected");
        }
        notifyAll();
        while (parent != null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(label + ": Waiting for connection to disconnect output stream");
            }
            wait();
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + ": Output stream disconnected");
        }
    }
    
    private void awaitParent() throws InterruptedIOException {
        if (parent == null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(label + ": Waiting for new output stream");
            }
            while (parent == null) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    throw new InterruptedIOException();
                }
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine(label + ": Got new output stream");
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
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + ": Output stream flushed");
        }
        notifyAll();
    }
}