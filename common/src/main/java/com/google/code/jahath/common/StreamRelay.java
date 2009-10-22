package com.google.code.jahath.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamRelay implements Runnable {
    private static final Log log = LogFactory.getLog(StreamRelay.class);
    
    private final String label;
    private final InputStream in;
    private final OutputStream out;
    
    public StreamRelay(String label, InputStream in, OutputStream out) {
        this.label = label;
        this.in = in;
        this.out = out;
    }
    
    public void run() {
        byte buf[] = new byte[4096];
        try {
            int n;
            while ((n = in.read(buf)) != -1) {
                if (log.isDebugEnabled()) {
                    StringBuilder dump = new StringBuilder(label);
                    dump.append('\n');
                    hexDump(dump, buf, n);
                    log.debug(dump);
                }
                out.write(buf, 0, n);
                out.flush();
            }
        } catch (IOException ex) {
            log.error(label, ex);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        log.info(label + " closed");
    }

    private static void hexDump(StringBuilder buffer, byte[] data, int length) {
        for (int start = 0; start < length; start += 16) {
            for (int i=0; i<16; i++) {
                int index = start+i;
                if (index < length) {
                    String hex = Integer.toHexString(data[start+i] & 0xFF);
                    if (hex.length() < 2) {
                        buffer.append('0');
                    }
                    buffer.append(hex);
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
                if (i == 7) {
                    buffer.append(' ');
                }
            }
            buffer.append(" |");
            for (int i=0; i<16; i++) {
                int index = start+i;
                if (index < length) {
                    int b = data[index] & 0xFF;
                    if (32 <= b && b < 128) {
                        buffer.append((char)b);
                    } else {
                        buffer.append('.');
                    }
                } else {
                    buffer.append(' ');
                }
            }
            buffer.append('|');
            buffer.append('\n');
        }
    }
}
