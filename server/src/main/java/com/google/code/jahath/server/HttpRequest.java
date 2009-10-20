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
package com.google.code.jahath.server;

import java.io.InputStream;

import com.google.code.jahath.common.http.Headers;

class HttpRequest {
    private final String path;
    private final InputStream in;
    private final Headers headers;
    
    public HttpRequest(String path, InputStream in, Headers headers) {
        this.path = path;
        this.in = in;
        this.headers = headers;
    }
    
    /**
     * Return the input stream for the request entity.
     * 
     * @return the input stream for the request entity, or <code>null</code> if the request has
     *         no content
     */
    public InputStream getInputStream() {
        return in;
    }
}
