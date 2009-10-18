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
package com.google.code.jahath.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JahathServer2 {
    private final ExecutorService executorService;
    private final Acceptor acceptor;
    
    public JahathServer2(int port) throws IOException {
        executorService = new ThreadPoolExecutor(6, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        acceptor = new Acceptor(new ServerSocket(port), executorService);
        executorService.execute(acceptor);
    }

    public final void stop() {
        acceptor.stop();
        executorService.shutdown();
    }
}
