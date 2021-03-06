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
package com.google.code.jahath.service.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.code.jahath.common.connection.Service;
import com.google.code.jahath.common.container.ExecutionEnvironment;
import com.googlecode.sisme.stream.Connection;

public class EchoService implements Service {
    public void handle(ExecutionEnvironment env, Connection connection) {
        try {
            byte[] buffer = new byte[4096];
            InputStream in = connection.getStreamSource().getInputStream();
            OutputStream out = connection.getStreamSink().getOutputStream();
            int c;
            while ((c = in.read(buffer)) != -1) {
                out.write(buffer, 0, c);
                out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
