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
package com.google.code.jahath;

import org.junit.Assert;
import org.junit.Test;

public class HostAddressTest {
    @Test
    public void testParse1() throws Exception {
        Assert.assertFalse(HostAddress.parse("1.2.3") instanceof IPv4Address);
    }

    @Test
    public void testParse2() throws Exception {
        Assert.assertFalse(HostAddress.parse("1.2.3.4.5") instanceof IPv4Address);
    }

    @Test
    public void testParse3() throws Exception {
        Assert.assertFalse(HostAddress.parse("1..3.5") instanceof IPv4Address);
    }

    @Test
    public void testParse4() throws Exception {
        Assert.assertFalse(HostAddress.parse("1.2.3.4.") instanceof IPv4Address);
    }
    
    @Test
    public void testParse5() throws Exception {
        Assert.assertEquals(new IPv4Address(new byte[] { 10, (byte)200, 0, 1 }), HostAddress.parse("10.200.0.1"));
    }
}
