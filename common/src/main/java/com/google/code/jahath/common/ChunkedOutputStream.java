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

public class ChunkedOutputStream extends OutputStream {
    private final CRLFOutputStream parent;
    
    public ChunkedOutputStream(OutputStream parent) {
        if (parent instanceof CRLFOutputStream) {
            this.parent = (CRLFOutputStream)parent;
        } else {
            this.parent = new CRLFOutputStream(parent);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        parent.writeLine(Integer.toString(len, 16));
        parent.write(b, off, len);
        parent.writeLine("");
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
        parent.writeLine("0");
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }
}
