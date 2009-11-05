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

import com.google.code.jahath.client.http.HttpResponse;
import com.google.code.jahath.common.http.HttpException;

class Util {
    static VCHException createException(HttpResponse response) throws HttpException {
        return new UnexpectedStatusCodeException(response.getStatusCode(), response.getReasonPhrase());
    }
    
    static String getRequiredHeader(HttpResponse response, String name) throws HttpException, MissingHeaderException {
        String value = response.getHeader(name);
        if (value != null) {
            return value;
        } else {
            throw new MissingHeaderException(name);
        }
    }
}