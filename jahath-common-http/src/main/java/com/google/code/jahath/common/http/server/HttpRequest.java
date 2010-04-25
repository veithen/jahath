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
package com.google.code.jahath.common.http.server;

import java.io.IOException;
import java.io.InputStream;

import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpInMessageImpl;
import com.google.code.jahath.common.http.HttpProtocolException;

public class HttpRequest extends HttpInMessageImpl {
    private final boolean secure;
    private String path;
    
    HttpRequest(InputStream in, boolean secure) throws IOException {
        super(in);
        this.secure = secure;
    }

    @Override
    protected void processStartLine(String line) throws HttpProtocolException {
        // TODO: do this properly!
        String[] parts = line.split(" ");
        path = parts[1];
    }

    public String getPath() throws HttpException {
        processHeaders();
        return path;
    }
    
    public String makeAbsoluteURI(String path) throws HttpException {
        StringBuilder buffer = new StringBuilder(secure ? "https" : "http");
        buffer.append("://");
        String host = getHeader(HttpConstants.Headers.HOST);
        // TODO: check if Host header is actually present
        buffer.append(host);
        if (!path.startsWith("/")) {
            buffer.append('/');
        }
        buffer.append(path);
        return buffer.toString();
    }
}
