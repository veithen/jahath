/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.jahath.common.http;

import java.io.IOException;
import java.io.InputStream;

public interface HttpInMessage {
    /**
     * Block until data for this message is available, i.e until the start of the message has been
     * received. Note that this method will not actually process any of the received data. Also a
     * subsequent call to any other method of this class may still block, typically because not all
     * headers have been received yet.
     * 
     * @return <code>true</code> if data is available, <code>false</code> if the end of the stream
     *         has been reached (because the peer closed the connection)
     * @throws IOException if an I/O error occurs
     */
    boolean await() throws IOException;
    
    String getHeader(String name) throws HttpException;
    
    /**
     * Return the input stream for the request entity.
     * 
     * @return the input stream for the request entity, or <code>null</code> if the request has
     *         no content
     * @throws IOException 
     */
    InputStream getInputStream() throws HttpException;
}
