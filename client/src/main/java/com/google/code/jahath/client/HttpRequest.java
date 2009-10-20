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
import java.io.OutputStream;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.http.ChunkedInputStream;
import com.google.code.jahath.common.http.ChunkedOutputStream;
import com.google.code.jahath.common.http.Headers;
import com.google.code.jahath.common.http.HttpOutputStream;

class HttpRequest {
    public enum Method { GET, POST };
    
    private final HttpOutputStream request;
    private final CRLFInputStream response;
    
    public HttpRequest(HttpOutputStream request, CRLFInputStream response) {
        this.request = request;
        this.response = response;
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
    
    public HttpResponse getResponse() throws IOException {
        String status = response.readLine();
        // TODO: process status line
        Headers headers = new Headers(response);
        // TODO: process headers
        return new HttpResponse(headers, new ChunkedInputStream(response));
    }
    
    public HttpResponse execute() throws IOException {
        request.writeLine("");
        request.flush();
        return getResponse();
    }
}
