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
package com.google.code.jahath.tests;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.UUID;

import org.apache.felix.framework.FrameworkFactory;
import org.apache.felix.main.AutoProcessor;
import org.apache.felix.shell.ShellService;
import org.junit.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;

public class OSGiRuntime {
    private final Framework framework;
    private final File storageDir;
    private final Object shellService;
    
    public OSGiRuntime() throws Exception {
        File projectBuildDir = new File("target").getAbsoluteFile();
        File bundleDir = new File(projectBuildDir, "bundles");
        if (!bundleDir.exists()) {
            Assert.fail(bundleDir + " not found");
        }
        FrameworkFactory factory = new FrameworkFactory();
        Properties config = new Properties();
        config.setProperty(AutoProcessor.AUTO_DEPLOY_DIR_PROPERY, bundleDir.getPath());
        config.setProperty(AutoProcessor.AUTO_DEPLOY_ACTION_PROPERY, "install,start");
        storageDir = new File(projectBuildDir, "felix_" + UUID.randomUUID());
        config.setProperty(Constants.FRAMEWORK_STORAGE, storageDir.getPath());
        framework = factory.newFramework(config);
        framework.init();
        BundleContext bundleContext = framework.getBundleContext();
        AutoProcessor.process(config, bundleContext);
        framework.start();
//        Thread.sleep(2000);
        ServiceReference shellServiceRef = bundleContext.getServiceReference(ShellService.class.getName());
        shellService = bundleContext.getService(shellServiceRef);
    }
    
    public void cmd(String commandLine) throws Exception {
        System.out.println("-> " + commandLine);
        Method method = shellService.getClass().getMethod("executeCommand", String.class, PrintStream.class, PrintStream.class);
        method.setAccessible(true); // Not exactly sure why this is necessary
        method.invoke(shellService, commandLine, System.out, System.err);
    }
    
    public void stop() throws Exception {
        framework.stop();
        // TODO: delete storage dir
    }
}
