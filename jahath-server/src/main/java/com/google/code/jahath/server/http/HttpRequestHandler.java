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
package com.google.code.jahath.server.http;

import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.google.code.jahath.common.http.HttpException;

public interface HttpRequestHandler {
    void handle(ExecutionEnvironment env, HttpRequest request, HttpResponse response) throws HttpException;
}
