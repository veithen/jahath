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
package com.google.code.jahath.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.google.code.jahath.server.Session;

public class EchoSession implements Session {
    private final PipedOutputStream out;
    private final PipedInputStream in;
    
    public EchoSession() throws IOException {
        out = new PipedOutputStream();
        in = new PipedInputStream(out);
    }

    public OutputStream getOutputStream() {
        return out;
    }
    
    public InputStream getInputStream() {
        return in;
    }
}
