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

public abstract class Type {
    public static final Type STRING = new Type() {
        @Override
        public Object parse(String s) throws ParseException {
            return s;
        }
    };
    
    public static final Type INTEGER = new Type() {
        @Override
        public Object parse(String s) throws ParseException {
            return Integer.valueOf(s); // TODO: exception handling
        }
    };
    
    public abstract Object parse(String s) throws ParseException;
}
