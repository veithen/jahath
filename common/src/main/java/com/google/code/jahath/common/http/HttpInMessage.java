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
import java.io.InputStream;

import com.google.code.jahath.common.CRLFInputStream;

public abstract class HttpInMessage {
    private final CRLFInputStream in;
    private Headers headers;
    private InputStream contentStream;

    public HttpInMessage(InputStream in) throws IOException {
        this.in = new CRLFInputStream(in);
    }

    protected abstract void processFirstLine(String line);
    
    public void await() throws IOException {
        if (headers == null) {
            processFirstLine(in.readLine());
            headers = new Headers(in);
            // TODO: this is of course not exhaustive...
            if ("chunked".equals(headers.getHeader("Transfer-Encoding"))) {
                contentStream = new ChunkedInputStream(in);
            } else {
                contentStream = null;
            }
        }
    }
    
    public String getHeader(String name) throws IOException {
        await();
        return headers.getHeader(name);
    }
    
    /**
     * Return the input stream for the request entity.
     * 
     * @return the input stream for the request entity, or <code>null</code> if the request has
     *         no content
     * @throws IOException 
     */
    public InputStream getInputStream() throws IOException {
        await();
        return contentStream;
    }
}
