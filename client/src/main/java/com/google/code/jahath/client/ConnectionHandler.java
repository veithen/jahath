package com.google.code.jahath.client;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.code.jahath.common.CRLFInputStream;
import com.google.code.jahath.common.CRLFOutputStream;
import com.google.code.jahath.common.ChunkedInputStream;
import com.google.code.jahath.common.ChunkedOutputStream;
import com.google.code.jahath.common.Relay;

public class ConnectionHandler implements Runnable {
    private static final Log log = LogFactory.getLog(ConnectionHandler.class);
    
    private final Socket socket;
    private final ExecutorService executorService;
    
    public ConnectionHandler(Socket socket, ExecutorService executorService) {
        this.socket = socket;
        this.executorService = executorService;
    }

    public void run() {
        try {
            Socket outSocket = new Socket("localhost", 8280);
            CRLFOutputStream request = new CRLFOutputStream(outSocket.getOutputStream());
            CRLFInputStream response = new CRLFInputStream(outSocket.getInputStream());
            request.writeLine("POST / HTTP/1.1");
            request.writeLine("Host: localhost:8280");
            request.writeLine("Content-Type: application/octet-stream");
            request.writeLine("Transfer-Encoding: chunked");
            request.writeLine("Connection: keep-alive");
            request.writeLine("");
            String status = response.readLine();
            log.info(status);
            // TODO: process status line
            String header;
            while ((header = response.readLine()).length() > 0) {
                // TODO: process header line
            }
            Future<?> f1 = executorService.submit(new Relay("request", socket.getInputStream(), new ChunkedOutputStream(request)));
            Future<?> f2 = executorService.submit(new Relay("response", new ChunkedInputStream(response), socket.getOutputStream()));
            f1.get();
            f2.get();
            socket.close();
        } catch (Exception ex) {
            log.error("", ex);
        }
    }
}
