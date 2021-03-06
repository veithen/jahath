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

import java.util.concurrent.Future;

class ChildContainer implements Container {
    class ExecutionEnvironmentImpl implements ExecutionEnvironment {
        public void execute(Task task) {
            parentEnv.execute(task);
        }

        public Future<?> submit(Task task) {
            return parentEnv.submit(task);
        }
    }
    
    final ExecutionEnvironment parentEnv;
    final ExecutionEnvironment childEnv;
    
    public ChildContainer(ExecutionEnvironment env) {
        parentEnv = env;
        childEnv = new ExecutionEnvironmentImpl();
    }

    public ExecutionEnvironment getExecutionEnvironment() {
        return childEnv;
    }

    public void shutdown() {
        // TODO: need to destroy all resources acquired using the child ExecutionEnvironment
    }
}
