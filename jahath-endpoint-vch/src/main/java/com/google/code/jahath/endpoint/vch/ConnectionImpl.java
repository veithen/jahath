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
package com.google.code.jahath.endpoint.vch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.code.jahath.AbstractConnection;
import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.http.HttpRequest;
import com.google.code.jahath.http.HttpResponse;

class ConnectionImpl extends AbstractConnection {
    private final HttpClient httpClient;
    private final String connectionId;
    private final OutputStream out;
    private final InputStream in;
    
    public ConnectionImpl(HttpClient httpClient, String connectionId) {
        this.httpClient = httpClient;
        this.connectionId = connectionId;
        out = new VCHOutputStream(httpClient, connectionId);
        in = new VCHInputStream(httpClient, connectionId);
    }
    
    public OutputStream getOutputStream() {
        return out;
    }

    public InputStream getInputStream() {
        return in;
    }

    @Override
    protected void doClose() throws IOException {
        // TODO: need a unit test for this
        try {
            HttpRequest request = httpClient.createRequest(HttpRequest.Method.POST, "/connections/" + connectionId);
            HttpResponse response = request.execute();
            if (response.getStatusCode() == HttpConstants.StatusCodes.NO_CONTENT) {
                return;
            } else {
                throw Util.createException(response);
            }
        } catch (HttpException ex) {
            throw new VCHConnectionException(ex);
        }
    }
}
