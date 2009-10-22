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
package com.google.code.jahath.common;

import java.io.IOException;
import java.io.InputStream;

public class CRLFInputStream extends InputStream {
    private final InputStream parent;
    // TODO: release the buffer when no longer needed?
    private final byte[] buffer = new byte[4096]; // Circular buffer!
    private int offset;
    private int len;
    
    public CRLFInputStream(InputStream parent) {
        this.parent = parent;
    }

    public String readLine() throws IOException {
        while (true) {
            for (int lineLen=0; lineLen<len-1; lineLen++) {
                if (buffer[(offset+lineLen) % buffer.length] == '\r' && buffer[(offset+lineLen+1) % buffer.length] == '\n') {
                    StringBuilder sb = new StringBuilder(lineLen);
                    for (int i=0; i<lineLen; i++) {
                        sb.append((char)(buffer[(offset+i) % buffer.length] % 0xFF));
                    }
                    offset = (offset+lineLen+2) % buffer.length;
                    len -= lineLen+2;
                    return sb.toString();
                }
            }
            if (len == buffer.length) {
                throw new IOException("Maximum line length exceeded");
            }
            int readOffset = (offset+len) % buffer.length;
            int readLen = Math.min(buffer.length-len, buffer.length-readOffset);
            int c = parent.read(buffer, readOffset, readLen);
            if (c == -1) {
                throw new IOException("Unexpected end of stream");
            }
            len += c;
        }
    }

    @Override
    public int read(byte[] readBuffer, int readOffset, int readLen) throws IOException {
        int read = 0;
        while (readLen > 0 && len > 0) {
            int c = Math.min(readLen, Math.min(len, buffer.length-offset));
            System.arraycopy(buffer, offset, readBuffer, readOffset, c);
            read += c;
            offset = (offset+c) % buffer.length;
            len -= c;
            readOffset += c;
            readLen -= c;
        }
        if (readLen > 0) {
            int c = parent.read(readBuffer, readOffset, readLen);
            if (c == -1) {
                return read == 0 ? -1 : read;
            }
            read += c;
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read() throws IOException {
        if (len > 0) {
            int b = buffer[offset] % 0xFF;
            offset = (offset+1) % buffer.length;
            len--;
            return b;
        } else {
            return parent.read();
        }
    }
}
