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

public class IPv4AddressTest {
    private static final byte[] ADDRESS_1 = { 10, 25, 0, 1 };
    private static final byte[] ADDRESS_2 = { 10, 24, 1, 0 };
    
    @Test
    public void testEqualsPositive() {
        Assert.assertTrue(new IPv4Address(ADDRESS_1).equals(new IPv4Address(ADDRESS_1)));
    }
    
    @Test
    public void testEqualsNegative() {
        Assert.assertFalse(new IPv4Address(ADDRESS_1).equals(new IPv4Address(ADDRESS_2)));
    }
    
    @Test
    public void testEqualsNull() {
        Assert.assertFalse(new IPv4Address(ADDRESS_1).equals(null));
    }

    @Test
    public void testHashCode1() {
        Assert.assertTrue(new IPv4Address(ADDRESS_1).hashCode() == new IPv4Address(ADDRESS_1).hashCode());
    }

    @Test
    public void testHashCode2() {
        Assert.assertTrue(new IPv4Address(ADDRESS_1).hashCode() != new IPv4Address(ADDRESS_2).hashCode());
    }
    
    @Test
    public void testToString() {
        Assert.assertEquals("10.25.0.1", new IPv4Address(ADDRESS_1).toString());
    }
}
