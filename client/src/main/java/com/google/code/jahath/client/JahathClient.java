package com.google.code.jahath.client;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JahathClient {
    public static void main(String[] args) throws Exception {
        final ExecutorService executorService = new ThreadPoolExecutor(6, 30, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        ServerSocket ss = new ServerSocket(8180);
        while (true) {
            executorService.execute(new ConnectionHandler(ss.accept(), executorService));
        }
    }
}
