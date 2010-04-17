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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.output.ProxyOutputStream;

public class HttpOutputStream extends ProxyOutputStream {
    private static final Logger log = Logger.getLogger(HttpOutputStream.class.getName());
    
    private static final byte[] CRLF = new byte[] { '\r', '\n' };
    
    public HttpOutputStream(OutputStream proxy) {
        super(proxy);
    }
    
    public void writeLine(String s) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine(s);
        }
        int len = s.length();
        byte[] bytes = new byte[len+2];
        for (int i=0; i<len; i++) {
            bytes[i] = (byte)s.charAt(i);
        }
        bytes[len] = '\r';
        bytes[len+1] = '\n';
        write(bytes);
    }

    public void writeHeader(String name, String value) throws IOException {
        writeLine(name + ": " + value);
    }
    
    public void writeIntHeader(String name, int value) throws IOException {
        writeHeader(name, String.valueOf(value));
    }
    
    public void flushHeaders() throws IOException {
        write(CRLF);
    }
}
