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

public class Sequence implements CommandParser<Dictionary> {
    private final List<CommandParser<Dictionary>> parts = new ArrayList<CommandParser<Dictionary>>();
    
    public void add(CommandParser<Dictionary> part) {
        parts.add(part);
    }
    
    public void formatUsage(StringBuilder buffer) {
        boolean first = true;
        for (CommandParser<Dictionary> part : parts) {
            if (first) {
                first = false;
            } else {
                buffer.append(' ');
            }
            part.formatUsage(buffer);
        }
    }

    public void parse(CommandLine p, Dictionary dictionary) throws ParseException {
        for (CommandParser<Dictionary> part : parts) {
            part.parse(p, dictionary);
        }
    }
}
