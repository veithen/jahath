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
import java.util.HashMap;
import java.util.Map;

import com.google.code.jahath.common.CRLFInputStream;

public class Headers {
    private final Map<String,String> headers = new HashMap<String,String>();
    
    public Headers(CRLFInputStream in) throws IOException {
        String line;
        while ((line = in.readLine()).length() > 0) {
            int i = line.indexOf(':');
            if (i == -1) {
                throw new IOException("Invalid header");
            }
            int j = i+1;
            // TODO: potential ArrayIndexOutOfBoundsException here!
            while (line.charAt(j) == ' ') {
                j++;
            }
            headers.put(line.substring(0, i), line.substring(j));
        }
    }
    
    public String getHeader(String name) {
        return headers.get(name);
    }
    
    public Integer getIntHeader(String name) {
        String value = headers.get(name);
        return value == null ? null : Integer.valueOf(value);
    }

    @Override
    public String toString() {
        return headers.toString();
    }
}
