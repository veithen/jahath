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

public class CommandLine {
    private final String line;
    private String nextArg;
    private int pos;
    
    public CommandLine(String line) {
        this.line = line;
    }
    
    private void ensureNext() {
        if (pos != -1 && nextArg == null) {
            int idx = line.indexOf(' ', pos);
            if (idx == -1) {
                nextArg = line.substring(pos);
                pos = -1;
            } else {
                nextArg = line.substring(pos, idx);
                idx++;
                while (line.charAt(idx) == ' ') {
                    idx++;
                }
                pos = idx;
            }
        }
    }
    
    public boolean hasNext() {
        ensureNext();
        return nextArg != null;
    }
    
    public String consume() {
        ensureNext();
        String arg = nextArg;
        nextArg = null;
        return arg;
    }
}
