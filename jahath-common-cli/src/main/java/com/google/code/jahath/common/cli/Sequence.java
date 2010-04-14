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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class Sequence implements ConfigSpec {
    private final List<ConfigSpec> parts = new ArrayList<ConfigSpec>();
    
    public void add(ConfigSpec part) {
        parts.add(part);
    }
    
    public void formatUsage(StringBuilder buffer) {
        boolean first = true;
        for (ConfigSpec part : parts) {
            if (first) {
                first = false;
            } else {
                buffer.append(' ');
            }
            part.formatUsage(buffer);
        }
    }

    public void parse(CommandLineParser p, Dictionary dictionary) throws ParseException {
        for (ConfigSpec part : parts) {
            part.parse(p, dictionary);
        }
    }
}
