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
package com.google.code.jahath.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DefaultFormatter extends Formatter {
    private static final long startMillis = System.currentTimeMillis();

    private static void rpad(StringBuilder buffer, String value, int len) {
        buffer.append(value);
        for (int i=value.length(); i<len; i++) {
            buffer.append(' ');
        }
    }
    
    private static void formatLong(StringBuilder buffer, long value, int digits) {
        char[] buf = new char[digits];
        for (int i=digits-1; i>=0; i--) {
            buf[i] = (char)('0' + value % 10);
            value /= 10;
        }
        buffer.append(buf);
    }
    
    @Override
    public String format(LogRecord record) {
        StringBuilder buffer = new StringBuilder();
        rpad(buffer, record.getLevel().getName(), 7);
        formatLong(buffer, record.getMillis()-startMillis, 6);
        buffer.append(" [");
        buffer.append(Thread.currentThread().getName());
        buffer.append("] ");
        buffer.append(record.getSourceClassName());
        buffer.append("#");
        buffer.append(record.getSourceMethodName());
        buffer.append(" :: ");
        buffer.append(formatMessage(record));
        buffer.append("\n");
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            StringWriter sw = new StringWriter();
            PrintWriter out = new PrintWriter(sw, false);
            record.getThrown().printStackTrace(out);
            out.flush();
            buffer.append(sw.toString());
        }
        return buffer.toString();
    }
}
