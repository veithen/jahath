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
package com.google.code.jahath.testutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.junit.Assert;

import com.google.code.jahath.Connection;

public class EchoTestUtil {
    private EchoTestUtil() {}
    
    public static void testEcho(Connection connection) throws IOException {
        OutputStream out = connection.getOutputStream();
        InputStream in = connection.getInputStream();
        Random random = new Random();
        byte[] buffer1 = new byte[512];
        byte[] buffer2 = new byte[512];
        for (int i=0; i<100; i++) {
            random.nextBytes(buffer1);
            out.write(buffer1);
            out.flush();
            // TODO: not entirely correct
            Assert.assertEquals(buffer2.length, in.read(buffer2));
            Assert.assertArrayEquals(buffer1, buffer2);
        }
    }
}
