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
package com.google.code.jahath.client.vch;

import java.io.IOException;

import com.google.code.jahath.common.http.HttpException;

/**
 * Indicates that an error occurred because of a problem with the underlying HTTP connection. This
 * exception wraps an {@link HttpException} and may be thrown by the streams returned by
 * {@link com.google.code.jahath.common.connection.Connection#getInputStream()} and
 * {@link com.google.code.jahath.common.connection.Connection#getOutputStream()}.
 * 
 * @author Andreas Veithen
 */
public class VCHConnectionException extends IOException {
    private static final long serialVersionUID = -7134597251596985153L;

    public VCHConnectionException(HttpException cause) {
        initCause(cause);
    }
}
