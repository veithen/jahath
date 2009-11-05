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

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

public class HttpConstantsTest {
    /**
     * Check that there is a reason phrase defined for every status code and that the name of the
     * constant matches the reason phrase.
     * 
     * @throws Exception
     */
    @Test
    public void testGetReasonPhrase() throws Exception {
        for (Field field : HttpConstants.StatusCodes.class.getFields()) {
            if (field.getType().equals(Integer.TYPE)) {
                String name = field.getName();
                int code = field.getInt(null);
                String phrase = HttpConstants.StatusCodes.getReasonPhrase(code);
                Assert.assertEquals(phrase.toUpperCase().replaceAll("[ -]", ""), name.replaceAll("_", ""));
            }
        }
    }
}
