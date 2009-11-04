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
package com.google.code.jahath.client.vch;

import java.io.IOException;
import java.io.OutputStream;

import com.google.code.jahath.client.http.HttpClient;
import com.google.code.jahath.client.http.HttpRequest;
import com.google.code.jahath.common.http.HttpException;

class VCHOutputStream extends OutputStream {
    private final HttpClient httpClient;
    private final String connectionId;
    private HttpRequest request;
    private OutputStream out;

    VCHOutputStream(HttpClient httpClient, String connectionId) {
        this.httpClient = httpClient;
        this.connectionId = connectionId;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (request == null) {
            try {
                request = httpClient.createRequest(HttpRequest.Method.POST, "/" + connectionId);
                out = request.getOutputStream("application/octet-stream");
            } catch (HttpException ex) {
                throw new VCHConnectionException(ex);
            }
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
        try {
            request.getResponse();
        } catch (HttpException ex) {
            throw new VCHConnectionException(ex);
        }
        request = null;
        out = null;
    }
}