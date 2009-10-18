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

import org.apache.commons.io.output.ProxyOutputStream;

public class CRLFOutputStream extends ProxyOutputStream {
    private static final byte[] CRLF = new byte[] { '\r', '\n' };
    
    public CRLFOutputStream(OutputStream proxy) {
        super(proxy);
    }
    
    public void writeLine(String s) throws IOException {
        write(s.getBytes("ascii"));
        write(CRLF);
    }
}
