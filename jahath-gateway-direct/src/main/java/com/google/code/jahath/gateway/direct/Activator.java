package com.google.code.jahath.gateway.direct;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.code.jahath.Gateway;

public class Activator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
	    Properties props = new Properties();
	    props.put("name", "direct");
	    context.registerService(Gateway.class.getName(), new DirectGateway(), props);
	}

	public void stop(BundleContext context) throws Exception {
	}
}
