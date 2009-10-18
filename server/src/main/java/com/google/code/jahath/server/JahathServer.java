package com.google.code.jahath.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpHandler;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.jetty.Server;

import com.google.code.jahath.common.Relay;

public class JahathServer {
    private static final Log log = LogFactory.getLog(JahathServer.class);
    
    public static void main(String[] args) throws Exception {
        final ExecutorService executorService = new ThreadPoolExecutor(6, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Server server = new Server();
        SocketListener listener = new SocketListener();
        listener.setPort(8280);
        server.addListener(listener);
        HttpContext context = new HttpContext(server, "/");
        HttpHandler handler = new AbstractHttpHandler() {
            public void handle(String pathInContext, String pathParams,
                    HttpRequest request, HttpResponse response) throws HttpException,
                    IOException {
                String remoteHost = request.getRemoteHost();
                String targetHost = "svn.apache.org";
                int targetPort = 80;
                Socket socket = new Socket(targetHost, targetPort);
                response.commit();
                Future<?> f1 = executorService.submit(new Relay(remoteHost + " -> " + targetHost + ":" + targetPort, request.getInputStream(), socket.getOutputStream()));
                Future<?> f2 = executorService.submit(new Relay(targetHost + ":" + targetPort + " -> " + remoteHost, socket.getInputStream(), response.getOutputStream()));
                try {
                    f1.get();
                    f2.get();
                } catch (Exception ex) {
                    log.error("", ex);
                }
                log.info("Handled");
                request.setHandled(true);
            }
        };
        context.addHandler(handler);
//        handler.start();
        server.start();
    }
}
