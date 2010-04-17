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
import java.util.Random;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.testutils.CRC;

public class Util {
    private Util() {}
    
    public static void writeRandomData(OutputStream out, CRC crc) throws IOException {
        Random random = new Random();
        for (int i=0; i<100; i++) {
            int len = 16 + random.nextInt(4080);
            byte[] buffer = new byte[len];
            random.nextBytes(buffer);
            out.write(buffer);
            out.flush();
            crc.update(buffer);
        }
    }
    
    public static void consumeHeaders(CRLFInputStream in) throws IOException {
        while (true) {
            String line = in.readLine();
            if (line.length() == 0) {
                break;
            }
        }
    }
}
