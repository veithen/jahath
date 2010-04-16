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

import java.util.Dictionary;

public abstract class Argument<T> implements CommandParser<Dictionary> {
    private final String key;
    
    public Argument(String key) {
        this.key = key;
    }

    public final void formatUsage(StringBuilder buffer) {
        buffer.append('<');
        buffer.append(key);
        buffer.append('>');
    }

    public final void parse(CommandLine p, Dictionary dictionary) throws ParseException {
        String value = p.consume();
        if (value == null) {
            throw new ParseException("Expected argument '" + key + "'");
        } else {
            dictionary.put(key, parse(value));
        }
    }
    
    protected abstract T parse(String value) throws ParseException;
}
