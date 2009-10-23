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
package com.google.code.jahath.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingOutputStream extends OutputStream {
    private final OutputStream parent;
    private final Logger log;
    private final String label;

    public LoggingOutputStream(OutputStream parent, Logger log, String label) {
        this.parent = parent;
        this.log = log;
        this.label = label;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            HexDump.log(log, label, b, off, len);
        }
        parent.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            HexDump.log(log, label, b, 0, b.length);
        }
        parent.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + " - writing single byte " + b);
        }
        parent.write(b);
    }

    @Override
    public void flush() throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + " - flushing");
        }
        parent.flush();
    }

    @Override
    public void close() throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine(label + " - closing");
        }
        parent.close();
    }
}
