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
package com.google.code.jahath.endpoint.vch;

/**
 * Indicates a VC/H protocol error, i.e. an unexpected HTTP response. An exception of this class is
 * thrown when the VC/H server sent a syntactically correct HTTP response that doesn't conform to
 * the VC/H protocol specification. There are several possible root causes that may trigger this
 * exception:
 * <ul>
 *   <li>There is a bug in the VC/H server or client implementation.
 *   <li>The server is not a VC/H server.
 *   <li>An HTTP proxy transformed the response so that it could not be understood by the client.
 * </ul>
 * 
 * @author Andreas Veithen
 */
public class VCHProtocolException extends VCHException {
    private static final long serialVersionUID = -8238996230611407613L;

    public VCHProtocolException(String message) {
        super(message);
    }
}
