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

public class DnsAddressTest {
    @Test
    public void testEqualsPositive() {
        Assert.assertTrue(new DnsAddress("example.org").equals(new DnsAddress("example.org")));
    }
    
    @Test
    public void testEqualsNegative() {
        Assert.assertFalse(new DnsAddress("example1.org").equals(new DnsAddress("example2.org")));
    }
    
    @Test
    public void testEqualsNull() {
        Assert.assertFalse(new DnsAddress("example.org").equals(null));
    }

    @Test
    public void testHashCode1() {
        Assert.assertTrue(new DnsAddress("example.org").hashCode() == new DnsAddress("example.org").hashCode());
    }

    @Test
    public void testHashCode2() {
        Assert.assertTrue(new DnsAddress("example1.org").hashCode() != new DnsAddress("example2.org").hashCode());
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("www.google.com", new DnsAddress("www.google.com").toString());
    }
}
