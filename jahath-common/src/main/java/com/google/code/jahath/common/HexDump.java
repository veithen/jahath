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

import java.util.logging.Logger;

public class HexDump {
    public static void hexDump(StringBuilder buffer, byte[] data, int offset, int length) {
        for (int start = 0; start < length; start += 16) {
            for (int i=0; i<16; i++) {
                int index = start+i;
                if (index < length) {
                    String hex = Integer.toHexString(data[offset+start+i] & 0xFF);
                    if (hex.length() < 2) {
                        buffer.append('0');
                    }
                    buffer.append(hex);
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
                if (i == 7) {
                    buffer.append(' ');
                }
            }
            buffer.append(" |");
            for (int i=0; i<16; i++) {
                int index = start+i;
                if (index < length) {
                    int b = data[offset+index] & 0xFF;
                    if (32 <= b && b < 128) {
                        buffer.append((char)b);
                    } else {
                        buffer.append('.');
                    }
                } else {
                    buffer.append(' ');
                }
            }
            buffer.append('|');
            buffer.append('\n');
        }
    }
    
    // TODO: we should be able to specify the log level here!
    public static void log(Logger log, String label, byte[] data, int offset, int length) {
        StringBuilder dump = new StringBuilder(label);
        dump.append('\n');
        HexDump.hexDump(dump, data, offset, length);
        log.fine(dump.toString());
    }
}
