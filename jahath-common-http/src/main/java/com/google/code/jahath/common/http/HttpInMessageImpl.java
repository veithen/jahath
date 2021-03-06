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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.io.EndOfStreamListenerInputStream;

public abstract class HttpInMessageImpl extends HttpMessage implements HttpInMessage {
    private static final Logger log = Logger.getLogger(HttpInMessage.class.getName());
    
    private final CRLFInputStream in;
    private Headers headers;
    private InputStream contentStream;

    public HttpInMessageImpl(InputStream in) {
        this.in = new CRLFInputStream(in);
    }

    /**
     * Process the start-line of the HTTP message. The start-line is either the Request-Line or the
     * Status-Line, depending on the type of message. See section 4.1 of RFC2616.
     * 
     * @param line
     *            the start-line
     * @throws HttpProtocolException
     *             if the start-line doesn't conform to the HTTP specifications
     */
    protected abstract void processStartLine(String line) throws HttpProtocolException;
    
    public boolean await() throws IOException {
        return status != Status.START || in.awaitInput();
    }
    
    protected void processHeaders() throws HttpException {
        if (status == Status.START) {
            try {
                processStartLine(in.readLine());
                headers = new Headers(in);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("HTTP headers: " + headers);
                }
                Integer contentLength = headers.getIntHeader(HttpConstants.Headers.CONTENT_LENGTH);
                if (contentLength != null) {
                    if (contentLength == 0) {
                        contentStream = null;
                    } else {
                        contentStream = new LengthLimitedInputStream(in, contentLength);
                    }
                } else if ("chunked".equals(headers.getHeader(HttpConstants.Headers.TRANSFER_ENCODING))) {
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
    
    public InputStream getInputStream() throws HttpException {
        processHeaders();
        status = Status.STREAMING;
        return contentStream;
    }
}
