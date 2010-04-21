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
package com.google.code.jahath.resolver;

import com.google.code.jahath.DnsAddress;
import com.google.code.jahath.IPAddress;

public interface Resolver {
    /**
     * Resolve the given DNS address to an IP address.
     * 
     * @param address the DNS address to resolve
     * @return the corresponding IP address or <code>null</code> if the name was not found
     */
    IPAddress resolve(DnsAddress address);
}
