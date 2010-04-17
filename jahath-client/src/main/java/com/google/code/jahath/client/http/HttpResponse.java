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
package com.google.code.jahath.client.http;

import java.io.InputStream;

import com.google.code.jahath.common.http.HttpConstants;
import com.google.code.jahath.common.http.HttpException;
import com.google.code.jahath.common.http.HttpInMessage;
import com.google.code.jahath.common.http.HttpProtocolException;

public class HttpResponse extends HttpInMessage {
    private int statusCode;
    private String reasonPhrase;
    
    HttpResponse(InputStream in) {
        super(in);
    }

    @Override
    protected void processStartLine(String line) throws HttpProtocolException {
        int i1 = line.indexOf(' ');
        int i2 = line.indexOf(' ', i1+1);
        if (i1 == -1 || i2 == -1) {
            throw new HttpProtocolException("Malformed status line: doesn't match Status-Line production");
        }
        
        String httpVersion = line.substring(0, i1);
        if (!httpVersion.equals(HttpConstants.HTTP_VERSION_1_1)) {
            throw new HttpProtocolException("Unsupported HTTP version '" + httpVersion + "'");
        }
        
        String statusCodeString = line.substring(i1+1, i2);
        if (statusCodeString.length() != 3) {
            statusCode = -1;
        } else {
            try {
                statusCode = Integer.parseInt(statusCodeString);
            } catch (NumberFormatException ex) {
                statusCode = -1;
            }
        }
        if (statusCode == -1) {
            throw new HttpProtocolException("Invalid status code '" + statusCodeString + "'");
        }
        
        reasonPhrase = line.substring(i2+1);
    }

    public int getStatusCode() throws HttpException {
        processHeaders();
        return statusCode;
    }

    public String getReasonPhrase() throws HttpException {
        processHeaders();
        return reasonPhrase;
    }
}
