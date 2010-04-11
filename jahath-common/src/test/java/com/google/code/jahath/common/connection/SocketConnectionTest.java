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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

import com.google.code.jahath.Connection;
import com.google.code.jahath.ConnectionClosedLocallyException;

public class SocketConnectionTest {
    private static class ExceptionCatcher implements Runnable {
        private final Connection connection;
        private volatile IOException exception;
        
        public ExceptionCatcher(Connection connection) {
            this.connection = connection;
        }

        public void run() {
            try {
                connection.getInputStream().read();
            } catch (IOException ex) {
                exception = ex;
            }
        }
        
        public IOException getException() throws InterruptedException {
            return exception;
        }
    }
    
    @Test
    public void testSocketClosedLocallyDuringRead() throws Exception {
        ServerSocket serverSocket = new ServerSocket(5555);
        try {
            Socket remoteSocket = new Socket("localhost", 5555);
            try {
                Socket localSocket = serverSocket.accept();
                try {
                    SocketConnection connection = new SocketConnection(localSocket);
                    ExceptionCatcher catcher = new ExceptionCatcher(connection);
                    Thread thread = new Thread(catcher);
                    thread.start();
                    // Give the thread the time to enter the read method
                    Thread.sleep(100);
                    connection.close();
                    thread.join();
                    IOException exception = catcher.getException();
                    Assert.assertNotNull(exception);
                    Assert.assertEquals(ConnectionClosedLocallyException.class, exception.getClass());
                } finally {
                    localSocket.close();
                }
            } finally {
                remoteSocket.close();
            }
        } finally {
            serverSocket.close();
        }
    }
}
