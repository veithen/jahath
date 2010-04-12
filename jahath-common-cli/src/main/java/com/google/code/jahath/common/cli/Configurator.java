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
package com.google.code.jahath.common.cli;

import java.io.IOException;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class Configurator {
    private final ConfigurationAdmin configurationAdmin;
    private final String factoryPid;
    private final String bundleLocation;
    
    public Configurator(ConfigurationAdmin configurationAdmin, String factoryPid, String bundleLocation) {
        this.configurationAdmin = configurationAdmin;
        this.factoryPid = factoryPid;
        this.bundleLocation = bundleLocation;
    }

    public Configuration createConfiguration() throws IOException {
        return configurationAdmin.createFactoryConfiguration(factoryPid, bundleLocation);
    }
}
