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
import java.io.OutputStream;

import com.google.code.jahath.common.io.CloseListenerOutputStream;

public class HttpOutMessage extends HttpMessage {
    private final HttpOutputStream request;
    private final HttpHeadersProvider headersProvider;

    public HttpOutMessage(HttpOutputStream request, HttpHeadersProvider headersProvider) {
        this.request = request;
        this.headersProvider = headersProvider;
    }

    protected void writeLine(String s) throws HttpException {
        try {
            request.writeLine(s);
        } catch (IOException ex) {
            throw new HttpConnectionException(ex);
        }
    }

    public void addHeader(String name, String value) throws HttpException {
        try {
            request.writeHeader(name, value);
        } catch (IOException ex) {
            throw new HttpConnectionException(ex);
        }
    }
    
    public void addIntHeader(String name, int value) throws HttpException {
        try {
            request.writeIntHeader(name, value);
        } catch (IOException ex) {
            throw new HttpConnectionException(ex);
        }
    }
    
    public OutputStream getOutputStream(String contentType) throws HttpException {
        try {
            if (headersProvider != null) {
                headersProvider.writeHeaders(this);
            }
            request.writeHeader(HttpConstants.Headers.CONTENT_TYPE, contentType);
            request.writeHeader(HttpConstants.Headers.TRANSFER_ENCODING, "chunked");
            request.flushHeaders();
            request.flush(); // TODO: remove this when no longer necessary
            status = Status.STREAMING;
            return new CloseListenerOutputStream(new ChunkedOutputStream(request)) {
                @Override
                protected void onClosed() {
                    status = Status.COMPLETE;
                }
            };
        } catch (IOException ex) {
            throw new HttpConnectionException(ex);
        }
    }

    protected void commit() throws HttpException {
        if (headersProvider != null) {
            headersProvider.writeHeaders(this);
        }
        try {
            request.writeHeader(HttpConstants.Headers.CONTENT_LENGTH, "0");
            request.writeLine("");
            request.flush();
            status = Status.COMPLETE;
        } catch (IOException ex) {
            throw new HttpConnectionException(ex);
        }
    }
}
