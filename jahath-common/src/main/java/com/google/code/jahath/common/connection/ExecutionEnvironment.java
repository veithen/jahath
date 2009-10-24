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
package com.google.code.jahath.common.connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutionEnvironment {
    private final ExecutorService executorService;

    public ExecutionEnvironment(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    public ExecutionEnvironment() {
        this(new ThreadPoolExecutor(20, 100, 30, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()));
    }

    public final void execute(Runnable command) {
        executorService.execute(command);
    }

    public final Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    public final void shutdown() {
        executorService.shutdown();
    }
}
