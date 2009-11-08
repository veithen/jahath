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
package com.google.code.jahath.server.http;

import java.io.UnsupportedEncodingException;

import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpHeadersProvider;
import com.google.code.jahath.common.http.HttpOutMessage;
import com.google.code.jahath.common.http.HttpOutputStream;

public class HttpResponse extends HttpOutMessage {
    HttpResponse(HttpOutputStream out, HttpHeadersProvider headersProvider) {
        super(out, headersProvider);
    }

    public void setStatus(int statusCode) throws HttpException {
        writeLine(HttpConstants.HTTP_VERSION_1_1 + " " + statusCode + " " + HttpConstants.StatusCodes.getReasonPhrase(statusCode));
    }
    
    public void sendError(int statusCode, String message) throws HttpException {
        setStatus(statusCode);
        try {
            send("text/plain; charset=\"UTF-8\"", message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex); // We should never get here
        }
    }
    
    // TODO: this is only to increase visibility; shall we keep it like this?
    @Override
    public void commit() throws HttpException {
        super.commit();
    }
}
