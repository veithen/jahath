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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.Connection;
import com.google.code.jahath.ConnectionClosedLocallyException;
import com.google.code.jahath.common.connection.Endpoint;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpHeadersProvider;
import com.google.code.jahath.common.http.HttpMessage;
import com.google.code.jahath.common.http.HttpOutMessage;
import com.google.code.jahath.common.http.HttpOutputStream;
import com.google.code.jahath.common.io.ErrorListenerInputStream;
import com.google.code.jahath.common.io.ErrorListenerOutputStream;

class HttpEndpoint implements Endpoint, HttpHeadersProvider {
    private static final Logger log = Logger.getLogger(HttpEndpoint.class.getName());
    
    private final HttpRequestHandler requestHandler;

    public HttpEndpoint(HttpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void writeHeaders(HttpOutMessage message) throws HttpException {
        message.addHeader(HttpConstants.Headers.CONNECTION, "keep-alive");
    }

    public void handle(ExecutionEnvironment env, Connection connection) {
        try {
            ErrorListenerInputStream in = new ErrorListenerInputStream(connection.getInputStream());
            ErrorListenerOutputStream out = new ErrorListenerOutputStream(connection.getOutputStream());
            log.fine("New connection");
            while (true) {
                HttpRequest httpRequest = new HttpRequest(in, connection.isSecure());
                try {
                    if (!httpRequest.await()) {
                        break;
                    }
                } catch (ConnectionClosedLocallyException ex) {
                    break;
                }
                log.fine("Start processing new request");
                // TODO: should wrapping the stream as an HttpOutputStream be done here or in HttpRequest??
                HttpOutputStream response = new HttpOutputStream(out);
                HttpResponse httpResponse = new HttpResponse(response, this);
                requestHandler.handle(env, httpRequest, httpResponse);
                log.fine("Finished processing request");
                if (in.isError() || out.isError()) {
                    log.fine("Closing connection because of previous error");
                    break;
                }
                if (httpRequest.getStatus() != HttpMessage.Status.COMPLETE ||
                        httpResponse.getStatus() != HttpMessage.Status.COMPLETE) {
                    log.warning("Closing connection because the handler didn't completely process the request (request status: "
                            + httpRequest.getStatus() + ", response status: " + httpResponse.getStatus() + ")");
                    break;
                }
                response.flush();
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, "", ex);
        }
    }
}
