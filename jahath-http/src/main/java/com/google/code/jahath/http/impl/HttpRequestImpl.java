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
package com.google.code.jahath.http.impl;

import java.io.InputStream;

import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpOutMessageImpl;
import com.google.code.jahath.common.http.HttpOutputStream;
import com.google.code.jahath.http.HttpRequest;
import com.google.code.jahath.http.HttpResponse;

public class HttpRequestImpl extends HttpOutMessageImpl implements HttpRequest {
    private final InputStream response;
    
    HttpRequestImpl(HttpOutputStream request, InputStream response) {
        super(request, null);
        this.response = response;
    }
    
    public HttpResponse getResponse() throws HttpException {
        return new HttpResponseImpl(response);
    }
    
    public HttpResponse execute() throws HttpException {
        commit();
        return getResponse();
    }
}
