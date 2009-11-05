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

import org.apache.commons.io.input.ProxyInputStream;

public abstract class EndOfStreamListenerInputStream extends ProxyInputStream {
    public EndOfStreamListenerInputStream(InputStream proxy) {
        super(proxy);
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        if (b == -1) {
            onEndOfStream();
        }
        return b;
    }

    @Override
    public int read(byte[] bts, int st, int end) throws IOException {
        int c = super.read(bts, st, end);
        if (c == -1) {
            onEndOfStream();
        }
        return c;
    }

    @Override
    public int read(byte[] bts) throws IOException {
        return read(bts, 0, bts.length);
    }
    
    protected abstract void onEndOfStream();
}
