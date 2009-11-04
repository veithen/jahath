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
package com.google.code.jahath.client.http;

import java.io.InputStream;

import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpOutMessage;
import com.google.code.jahath.common.http.HttpOutputStream;

public class HttpRequest extends HttpOutMessage {
    public enum Method { GET, POST };
    
    private final InputStream response;
    
    HttpRequest(HttpOutputStream request, InputStream response) {
        super(request, null);
        this.response = response;
    }
    
    public HttpResponse getResponse() throws HttpException {
        return new HttpResponse(response);
    }
    
    public HttpResponse execute() throws HttpException {
        commit();
        return getResponse();
    }
}
