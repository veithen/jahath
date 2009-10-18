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

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

public class CRLFInputStreamTest {
    @Test
    public void testReadLine() throws Exception {
        CRLFInputStream in = new CRLFInputStream(new ByteArrayInputStream("test1\r\ntest2\r\n".getBytes("ascii")));
        Assert.assertEquals("test1", in.readLine());
        Assert.assertEquals("test2", in.readLine());
    }

    @Test
    public void testRead() throws Exception {
        CRLFInputStream in = new CRLFInputStream(new ByteArrayInputStream("test\r\nABCDEFGHI".getBytes("ascii")));
        Assert.assertEquals("test", in.readLine());
        byte[] buffer = new byte[4];
        Assert.assertEquals(4, in.read(buffer));
        Assert.assertEquals('A', buffer[0]);
        Assert.assertEquals(4, in.read(buffer));
        Assert.assertEquals('E', buffer[0]);
        Assert.assertEquals(1, in.read(buffer));
        Assert.assertEquals('I', buffer[0]);
    }
}
