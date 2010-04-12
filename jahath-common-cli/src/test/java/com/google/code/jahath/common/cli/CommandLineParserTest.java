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
package com.google.code.jahath.common.cli;

import org.junit.Assert;
import org.junit.Test;

public class CommandLineParserTest {
    @Test
    public void testSimple() {
        CommandLineParser p = new CommandLineParser("command arg1 arg2");
        Assert.assertTrue(p.hasNext());
        Assert.assertEquals("command", p.consume());
        Assert.assertTrue(p.hasNext());
        Assert.assertEquals("arg1", p.consume());
        Assert.assertTrue(p.hasNext());
        Assert.assertEquals("arg2", p.consume());
        Assert.assertFalse(p.hasNext());
        Assert.assertNull(p.consume());
    }
}
