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

public class AddressParseException extends Exception {
    private static final long serialVersionUID = 1380380661995020103L;

    public AddressParseException() {
        super();
    }

    public AddressParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressParseException(String message) {
        super(message);
    }

    public AddressParseException(Throwable cause) {
        super(cause);
    }
}
