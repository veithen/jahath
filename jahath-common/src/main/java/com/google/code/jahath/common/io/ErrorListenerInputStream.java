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

public class ErrorListenerInputStream extends InputStream {
    private final InputStream parent;
    private boolean error;

    public ErrorListenerInputStream(InputStream parent) {
        this.parent = parent;
    }

    public boolean isError() {
        return error;
    }

    @Override
    public int available() throws IOException {
        try {
            return parent.available();
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            parent.close();
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public int read() throws IOException {
        try {
            return parent.read();
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            return parent.read(b, off, len);
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        try {
            return parent.read(b);
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public void reset() throws IOException {
        try {
            parent.reset();
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return parent.skip(n);
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }
}
