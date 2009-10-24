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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.connection.Connection;
import com.google.code.jahath.common.connection.ExecutionEnvironment;

public class ConnectionRelay implements Runnable {
    private final Logger log;
    private final ExecutionEnvironment env;
    private final Connection connection1;
    private final String label1;
    private final Connection connection2;
    private final String label2;

    // TODO: we should ExecutionEnvironment here
    public ConnectionRelay(Logger log, ExecutionEnvironment env, Connection connection1, String label1,
            Connection connection2, String label2) {
        this.log = log;
        this.env = env;
        this.connection1 = connection1;
        this.label1 = label1;
        this.connection2 = connection2;
        this.label2 = label2;
    }

    public void run() {
        try {
            Future<?> f1 = env.submit(new StreamRelay(log, label1 + " -> " + label2, connection1.getInputStream(), connection2.getOutputStream()));
            Future<?> f2 = env.submit(new StreamRelay(log, label2 + " -> " + label1, connection2.getInputStream(), connection1.getOutputStream()));
            f1.get();
            f2.get();
        } catch (Exception ex) {
            // TODO: handle exceptions properly!
            log.log(Level.SEVERE, "", ex);
        }
    }
}
