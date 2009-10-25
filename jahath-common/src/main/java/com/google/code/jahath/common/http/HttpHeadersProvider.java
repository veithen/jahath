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

/**
 * Provider of headers to be added to an {@link HttpOutMessage}. HTTP requests and responses contain
 * two categories of headers:
 * <ul>
 * <li>Headers related to the request or response entity, such as <tt>Content-Type</tt>.
 * <li>Headers used for connection management, such as <tt>Connection</tt> and
 * <tt>Proxy-Connection</tt>.
 * </ul>
 * These two types of headers are managed at different levels in the client or server
 * implementation. This interface allows the code responsible for connection management to inject
 * headers as necessary. It may also be used to add headers such as <tt>User-Agent</tt> that should
 * be added to all requests or responses.
 * 
 * @author Andreas Veithen
 */
public interface HttpHeadersProvider {
    void writeHeaders(HttpOutMessage message) throws IOException;
}
