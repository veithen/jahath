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
package com.google.code.jahath.client.vch;

import java.io.IOException;
import java.io.InputStream;

import com.google.code.jahath.client.http.HttpClient;
import com.google.code.jahath.client.http.HttpRequest;
import com.google.code.jahath.client.http.HttpResponse;
import com.google.code.jahath.common.http.HttpException;

class VCHInputStream extends InputStream {
    private final HttpClient httpClient;
    private final String connectionId;
    private InputStream in;

    VCHInputStream(HttpClient httpClient, String connectionId) {
        this.httpClient = httpClient;
        this.connectionId = connectionId;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        while (true) {
            if (in == null) {
                try {
                    HttpRequest request = httpClient.createRequest(HttpRequest.Method.POST, "/connections/" + connectionId);
                    HttpResponse response = request.execute();
                    in = response.getInputStream();
                } catch (HttpException ex) {
                    throw new VCHConnectionException(ex);
                }
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