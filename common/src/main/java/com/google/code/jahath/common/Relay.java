package com.google.code.jahath.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Relay implements Runnable {
    private static final Log log = LogFactory.getLog(Relay.class);
    
    private final String tag;
    private final InputStream in;
    private final OutputStream out;
    
    public Relay(String tag, InputStream in, OutputStream out) {
        this.tag = tag;
        this.in = in;
        this.out = out;
    }
    
    public void run() {
        byte buf[] = new byte[4096];
        try {
            int n;
            while ((n = in.read(buf)) > 0) {
                System.out.write(buf, 0, n);
                out.write(buf, 0, n);
                out.flush();
            }
        } catch (IOException ex) {
            log.error(tag, ex);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        log.info(tag + " closed");
    }
}
