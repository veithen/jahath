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

import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.http.HttpGateway;
import com.google.code.jahath.http.HttpRequest;
import com.google.code.jahath.http.HttpResponse;
import com.google.code.jahath.tcp.SocketAddress;
import com.googlecode.sisme.stream.Connection;

public class VCHClient {
    private final HttpClient httpClient;
    
    public VCHClient(SocketAddress server, HttpGateway gateway) {
        httpClient = new HttpClient(server, gateway);
    }
    
    public Connection createConnection(String serviceName) throws VCHException {
        try {
            HttpRequest request = httpClient.createRequest(HttpRequest.Method.POST, "/services/" + serviceName);
            HttpResponse response = request.execute();
            switch (response.getStatusCode()) {
                case HttpConstants.StatusCodes.CREATED:
                    String location = Util.getRequiredHeader(response, HttpConstants.Headers.LOCATION);
                    String path = httpClient.getPath(location);
                    if (path == null) {
                        throw new VCHProtocolException("The server returned an unexpected value for the Location header ("
                                + location + "): the location identified by the URL is on a different server.");
                    }
                    if (!path.startsWith("/connections/")) {
                        throw new VCHProtocolException("The server returned a location (" + location + ") that doesn't conform to the VC/H specification");
                    }
                    String connectionId = path.substring(13);
                    if (!isValidConnectionId(connectionId)) {
                        throw new VCHProtocolException("The server returned an invalid connection ID (" + connectionId + ")");
                    }
                    return new ConnectionImpl(httpClient, connectionId);
                case HttpConstants.StatusCodes.NOT_FOUND:
                    throw new NoSuchServiceException(serviceName);
                default:
                    throw Util.createException(response);
            }
        } catch (HttpException ex) {
            throw new VCHConnectionException(ex);
        }
    }
    
    private static boolean isConnectionIdChar(char c) {
        return 'A' <= c && c <= 'Z' || 'a' <= c && c <= 'z' || '0' <= c && c <= '9' || c == '-' || c == '.' || c == '_' || c == ':' || c == '!' || c == '@';
    }
    
    private static boolean isValidConnectionId(String connectionId) {
        int len = connectionId.length();
        if (len < 10) {
            return false;
        } else {
            for (int i=0; i<len; i++) {
                if (!isConnectionIdChar(connectionId.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public void shutdown() {
        // TODO: probably the HttpClient class will have a shutdown method that needs to be called here
    }
}
