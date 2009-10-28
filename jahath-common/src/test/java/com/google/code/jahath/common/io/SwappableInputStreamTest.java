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
package com.google.code.jahath.common.io;

import java.io.ByteArrayInputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.input.CountingInputStream;
import org.junit.Assert;
import org.junit.Test;

import com.google.code.jahath.common.testutils.CRC;

public class SwappableInputStreamTest {
    @Test
    public void test() throws Throwable {
        final SwappableInputStream swappableInputStream = new SwappableInputStream("test");
        final CRC actualCRC = new CRC();
        final AtomicReference<Throwable> thrown = new AtomicReference<Throwable>();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    actualCRC.update(swappableInputStream);
                } catch (Throwable ex) {
                    thrown.set(ex);
                }
            }
        });
        thread.start();
        Random random = new Random();
        CRC expectedCRC = new CRC();
        for (int i=0; i<100; i++) {
            int len = 2048 + random.nextInt(4096);
            byte[] data = new byte[len];
            random.nextBytes(data);
            expectedCRC.update(data);
            CountingInputStream in = new CountingInputStream(new ByteArrayInputStream(data));
            swappableInputStream.swap(in);
            // Check that the stream has been consumed entirely
            Assert.assertEquals(len, in.getCount());
        }
        swappableInputStream.sendEndOfStream();
        thread.join();
        if (thrown.get() != null) {
            throw thrown.get();
        }
        Assert.assertEquals(expectedCRC.getValue(), actualCRC.getValue());
    }
}
