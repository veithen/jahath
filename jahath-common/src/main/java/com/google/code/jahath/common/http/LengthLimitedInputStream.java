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

public class LengthLimitedInputStream extends InputStream {
    private final InputStream parent;
    private int bytesToRead;
    
    public LengthLimitedInputStream(InputStream parent, int length) {
        this.parent = parent;
        bytesToRead = length;
    }

    @Override
    public int available() throws IOException {
        int available = parent.available();
        return available > bytesToRead ? bytesToRead : available;
    }

    @Override
    public int read() throws IOException {
        if (bytesToRead > 0) {
            int b = parent.read();
            if (b == -1) {
                throw new IOException("Unexpected end of stream");
            }
            bytesToRead--;
            return b;
        } else {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (bytesToRead == 0) {
            return -1;
        } else {
            int c = parent.read(b, off, Math.min(bytesToRead, len));
            if (c == -1) {
                throw new IOException("Unexpected end of stream");
            } else {
                bytesToRead -= c;
                return c;
            }
        }
    }

    @Override
    public long skip(long n) throws IOException {
        return parent.skip(Math.min(bytesToRead, n));
    }
}
