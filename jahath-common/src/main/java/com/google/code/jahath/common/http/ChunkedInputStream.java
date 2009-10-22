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
package com.google.code.jahath.common.http;

import java.io.IOException;
import java.io.InputStream;

import com.google.code.jahath.common.CRLFInputStream;

public class ChunkedInputStream extends InputStream {
    private final CRLFInputStream parent;
    
    /**
     * The number of bytes remaining in the current chunk. <code>-1</code> indicates that the
     * stream is positioned in between two chunks; <code>-2</code> indicates that the last
     * chunk has been read.
     */
    private int remaining = -1;

    public ChunkedInputStream(InputStream parent) {
        if (parent instanceof CRLFInputStream) {
            this.parent = (CRLFInputStream)parent;
        } else {
            this.parent = new CRLFInputStream(parent);
        }
    }
    
    private void beforeRead() throws IOException {
        if (remaining == -1) {
            String s = parent.readLine();
            try {
                remaining = Integer.parseInt(s, 16);
            } catch (NumberFormatException ex) {
                throw new IOException("Invalid chunk size");
            }
            if (remaining == 0) {
                remaining = -2;
            }
        }
    }
    
    private void afterRead() throws IOException {
        if (remaining == 0) {
            String s = parent.readLine();
            if (s.length() > 0) {
                throw new IOException("Invalid chunked encoding");
            }
            remaining = -1;
        }
    }

    @Override
    public int read() throws IOException {
        beforeRead();
        if (remaining == -2) {
            return -1;
        } else {
            int b = parent.read();
            remaining--;
            afterRead();
            return b;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        beforeRead();
        if (remaining == -2) {
            return -1;
        } else {
            int c = parent.read(b, off, Math.min(remaining, len));
            if (c == -1) {
                throw new IOException("Unexpected end of stream");
            }
            remaining -= c;
            afterRead();
            return c;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }
}
