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
import java.io.InputStream;

public class CRC {
    int value;
    
    public void update(byte[] b, int off, int len) {
        for (int i=0; i<len; i++) {
            value += b[off+i] & 0xFF;
        }
    }
    
    public void update(byte[] b) {
        update(b, 0, b.length);
    }

    public void update(InputStream in) throws IOException {
        byte[] buffer = new byte[2048];
        int c;
        while ((c = in.read(buffer)) != -1) {
            update(buffer, 0, c);
        }
    }

    public int getValue() {
        return value;
    }
}