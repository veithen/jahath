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
package com.google.code.jahath.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Session {
    class SessionOutputStream extends OutputStream {
        private HttpRequest request;
        private OutputStream out;

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (request == null) {
                request = client.createRequest(HttpRequest.Method.POST, "/" + sessionId);
                out = request.getOutputStream("application/octet-stream");
            }
            out.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            write(new byte[] { (byte)b });
        }

        @Override
        public void flush() throws IOException {
            out.close();
            // TODO: we need to consume the response somewhere!
            request.getResponse();
            request = null;
            out = null;
        }
    }
    
    class SessionInputStream extends InputStream {
        private InputStream in;

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            while (true) {
                if (in == null) {
                    HttpRequest request = client.createRequest(HttpRequest.Method.POST, "/" + sessionId);
                    HttpResponse response = request.execute();
                    in = response.getInputStream();
                }
                int c = in.read(b, off, len);
                if (c == -1) {
                    in = null;
                } else {
                    return c;
                }
            }
        }

        @Override
        public int read() throws IOException {
            byte[] b = new byte[1];
            int c = read(b);
            return c == -1 ? -1 : b[0] & 0xFF;
        }
    }
    
    final JahathClient client;
    final String sessionId;
    private final OutputStream out;
    private final InputStream in;
    
    public Session(JahathClient client, String sessionId) {
        this.client = client;
        this.sessionId = sessionId;
        out = new SessionOutputStream();
        in = new SessionInputStream();
    }
    
    public OutputStream getOutputStream() {
        return out;
    }

    public InputStream getInputStream() {
        return in;
    }
}
