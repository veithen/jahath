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
package com.google.code.jahath.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.io.EndOfStreamListenerInputStream;

public abstract class HttpInMessage extends HttpMessage {
    private static final Logger log = Logger.getLogger(HttpInMessage.class.getName());
    
    private final CRLFInputStream in;
    private Headers headers;
    private InputStream contentStream;

    public HttpInMessage(InputStream in) {
        this.in = new CRLFInputStream(in);
    }

    protected abstract void processFirstLine(String line) throws HttpProtocolException;
    
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
    public boolean await() throws IOException {
        return status != Status.START || in.awaitInput();
    }
    
    protected void processHeaders() throws HttpException {
        if (status == Status.START) {
            try {
                processFirstLine(in.readLine());
                headers = new Headers(in);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("HTTP headers: " + headers);
                }
                Integer contentLength = headers.getIntHeader(HttpConstants.H_CONTENT_LENGTH);
                if (contentLength != null) {
                    if (contentLength == 0) {
                        contentStream = null;
                    } else {
                        contentStream = new LengthLimitedInputStream(in, contentLength);
                    }
                } else if ("chunked".equals(headers.getHeader(HttpConstants.H_TRANSFER_ENCODING))) {
                    contentStream = new ChunkedInputStream(in);
                } else {
                    contentStream = null;
                }
                if (contentStream == null) {
                    status = Status.COMPLETE;
                } else {
                    status = Status.HEADERS_COMPLETE;
                    contentStream = new EndOfStreamListenerInputStream(contentStream) {
                        @Override
                        protected void onEndOfStream() {
                            status = Status.COMPLETE;
                        }
                    };
                }
            } catch (IOException ex) {
                throw new HttpConnectionException(ex);
            }
        }
    }
    
    public String getHeader(String name) throws HttpException {
        processHeaders();
        return headers.getHeader(name);
    }
    
    /**
     * Return the input stream for the request entity.
     * 
     * @return the input stream for the request entity, or <code>null</code> if the request has
     *         no content
     * @throws IOException 
     */
    public InputStream getInputStream() throws HttpException {
        processHeaders();
        status = Status.STREAMING;
        return contentStream;
    }
}
