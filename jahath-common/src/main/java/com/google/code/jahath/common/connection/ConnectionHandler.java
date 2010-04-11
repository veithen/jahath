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

import com.google.code.jahath.Connection;
import com.google.code.jahath.common.container.ExecutionEnvironment;

/**
 * Blocking connection handler.
 * 
 * @author Andreas Veithen
 */
public interface ConnectionHandler {
    /**
     * Handle a given connection. The method will block until the connection is completely handled,
     * i.e. until it is closed or ready to be closed. Note that the implementation may close the
     * connection but is not required to do so. It is the responsibility of the caller to do any
     * necessary cleanup.
     * 
     * @param env
     *            the execution environment to use when acquiring any managed resource (threads,
     *            etc.)
     * @param connection
     *            the connection to handle
     */
    void handle(ExecutionEnvironment env, Connection connection);
}
