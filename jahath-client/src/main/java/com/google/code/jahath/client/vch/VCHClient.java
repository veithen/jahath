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

import com.google.code.jahath.client.http.HttpClient;
import com.google.code.jahath.client.http.HttpRequest;
import com.google.code.jahath.client.http.HttpResponse;
import com.google.code.jahath.client.http.ProxyConfiguration;
import com.google.code.jahath.common.connection.Connection;
import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;

public class VCHClient {
    private final HttpClient httpClient;
    
    public VCHClient(String serverHost, int serverPort, ProxyConfiguration proxyConfiguration) {
        httpClient = new HttpClient(serverHost, serverPort, proxyConfiguration);
    }
    
    public Connection createConnection() throws VCHException {
        try {
            HttpRequest request = httpClient.createRequest(HttpRequest.Method.POST, "/");
            HttpResponse response = request.execute();
            switch (response.getStatusCode()) {
                case HttpConstants.SC_NO_CONTENT: // TODO: should actually be SC_CREATED
                    String location = Util.getRequiredHeader(response, HttpConstants.H_LOCATION);
                    String path = httpClient.getPath(location);
                    if (path == null) {
                        throw new VCHProtocolException("The server returned an unexpected value for the Location header ("
                                + location + "): the location identified by the URL is on a different server.");
                    }
                    if (!path.startsWith("/connections/")) {
                        throw new VCHProtocolException("The server returned a location (" + location + ") that doesn't conform to the VC/H specification");
                    }
                    String connectionId = path.substring(13);
                    // TODO: we should validate the connection ID --> need to specify the format in the specs
                    return new ConnectionImpl(httpClient, connectionId);
                case HttpConstants.SC_NOT_FOUND:
                    throw new NoSuchServiceException("???"); // TODO: we don't support service names yet
                default:
                    throw Util.createException(response);
            }
        } catch (HttpException ex) {
            throw new VCHConnectionException(ex);
        }
    }

    public void shutdown() {
        // TODO: probably the HttpClient class will have a shutdown method that needs to be called here
    }
}
