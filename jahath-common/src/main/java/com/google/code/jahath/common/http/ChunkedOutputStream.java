/*
 * Copyright 2009-2010 Andreas Veithen
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
import java.io.OutputStream;

public class ChunkedOutputStream extends OutputStream {
    private final OutputStream parent;
    private final byte[] sizeBytes = { 0, 0, 0, 0, 0, 0, 0, 0, '\r', '\n' };
    
    public ChunkedOutputStream(OutputStream parent) {
        this.parent = parent;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        int pos = 8;
        int num = len;
        while (num > 0) {
            int digit = num & 0xF;
            sizeBytes[--pos] = (byte)(digit < 10 ? '0' + digit : 'A' + digit - 10);
            num >>= 4;
        }
        parent.write(sizeBytes, pos, 10-pos);
        parent.write(b, off, len);
        parent.write(sizeBytes, 8, 2);
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[] { (byte)b });
    }

    @Override
    public void close() throws IOException {
        sizeBytes[7] = '0';
        parent.write(sizeBytes, 7, 3);
        // <-- Trailer would be written here
        parent.write(sizeBytes, 8, 2);
        parent.flush();
    }

    @Override
    public void flush() throws IOException {
        parent.flush();
    }
}
