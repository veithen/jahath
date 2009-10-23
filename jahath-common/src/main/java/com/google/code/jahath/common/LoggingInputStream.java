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
import java.io.InputStream;

import org.apache.commons.logging.Log;

public class LoggingInputStream extends InputStream{
    private final InputStream parent;
    private final Log log;
    private final String label;
    
    public LoggingInputStream(InputStream parent, Log log, String label) {
        this.parent = parent;
        this.log = log;
        this.label = label;
    }

    @Override
    public int read() throws IOException {
        return parent.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int c = parent.read(b, off, len);
        if (log.isDebugEnabled()) {
            if (c == -1) {
                log.debug(label + " - end of stream");
            } else {
                HexDump.log(log, label, b, off, c);
            }
        }
        return c;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public long skip(long n) throws IOException {
        return parent.skip(n);
    }

    @Override
    public int available() throws IOException {
        return parent.available();
    }

    @Override
    public boolean markSupported() {
        return parent.markSupported();
    }

    @Override
    public void mark(int readlimit) {
        parent.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        parent.reset();
    }

    @Override
    public void close() throws IOException {
        parent.close();
    }
}
