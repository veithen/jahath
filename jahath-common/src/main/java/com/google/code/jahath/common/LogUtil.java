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
package com.google.code.jahath.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {
    private LogUtil() {}
    
    public static InputStream log(InputStream parent, Logger log, Level level, String label) {
        if (log.isLoggable(level)) {
            return new LoggingInputStream(parent, log, level, label);
        } else {
            return parent;
        }
    }
    
    public static OutputStream log(OutputStream parent, Logger log, Level level, String label) {
        if (log.isLoggable(level)) {
            return new LoggingOutputStream(parent, log, level, label);
        } else {
            return parent;
        }
    }
}
