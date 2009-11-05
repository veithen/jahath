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
import java.io.OutputStream;

public class ErrorListenerOutputStream extends OutputStream {
    private final OutputStream parent;
    private boolean error;

    public ErrorListenerOutputStream(OutputStream parent) {
        this.parent = parent;
    }

    public boolean isError() {
        return error;
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
    public void flush() throws IOException {
        try {
            parent.flush();
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            parent.write(b, off, len);
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            parent.write(b);
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }

    @Override
    public void write(int b) throws IOException {
        try {
            parent.write(b);
        } catch (IOException ex) {
            error = true;
            throw ex;
        }
    }
}
