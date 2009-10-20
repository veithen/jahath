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
import java.io.OutputStream;

public class HttpOutMessage {
    private final HttpOutputStream request;

    public HttpOutMessage(HttpOutputStream request) {
        this.request = request;
    }

    public void addHeader(String name, String value) throws IOException {
        request.writeHeader(name, value);
    }
    
    public void addIntHeader(String name, int value) throws IOException {
        request.writeIntHeader(name, value);
    }
    
    public OutputStream getOutputStream(String contentType) throws IOException {
        request.writeHeader("Content-Type", contentType);
        request.writeHeader("Transfer-Encoding", "chunked");
        request.flushHeaders();
        request.flush(); // TODO: remove this when no longer necessary
        return new ChunkedOutputStream(request);
    }

    protected void commit() throws IOException {
        request.writeLine("");
        request.flush();
    }
}
