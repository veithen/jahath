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
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Models a bidirectional stream oriented connection as seen by one of the endpoints. This interface
 * is a generalization of {@link java.net.Socket}.
 * 
 * @author Andreas Veithen
 */
public interface Connection {
    /**
     * Get an input stream for this connection. The returned stream can be used to read data send by
     * the other end of the connection.
     * 
     * @return The input stream for the connection. For a given connection, multiple calls to this
     * method will return the same instance.
     * @throws IOException 
     */
    InputStream getInputStream() throws IOException;
    
    /**
     * Get an output stream for this connection. The returned stream can be used to transmit data to
     * the other end of the connection.
     * 
     * @return The output stream for the connection. For a given connection, multiple calls to this
     * method will return the same instance.
     * @throws IOException 
     */
    OutputStream getOutputStream() throws IOException;
}
