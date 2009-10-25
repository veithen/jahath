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
package com.google.code.jahath.common.container;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class RootContainer implements Container {
    class ExecutionEnvironmentImpl implements ExecutionEnvironment {
        public final void execute(Task task) {
            executorService.execute(wrapTask(task));
        }

        public final Future<?> submit(Task task) {
            // TODO not entirely correct: if the task is canceled it will still be in the activeTasks collection!
            return executorService.submit(wrapTask(task));
        }
    }
    
    final ExecutorService executorService;
    private final ExecutionEnvironment env;
    final Collection<Task> activeTasks = new HashSet<Task>();

    public RootContainer(ExecutorService executorService) {
        this.executorService = executorService;
        env = new ExecutionEnvironmentImpl();
    }

    public ExecutionEnvironment getExecutionEnvironment() {
        return env;
    }

    Runnable wrapTask(final Task task) {
        synchronized (activeTasks) {
            activeTasks.add(task);
        }
        return new Runnable() {
            public void run() {
                try {
                    task.run();
                } finally {
                    synchronized (activeTasks) {
                        activeTasks.remove(task);
                    }
                }
            }
        };
    }
    
    public void shutdown() throws InterruptedException {
        executorService.shutdown();
        synchronized (activeTasks) {
            for (Task task : activeTasks) {
                task.stop();
            }
        }
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
}
