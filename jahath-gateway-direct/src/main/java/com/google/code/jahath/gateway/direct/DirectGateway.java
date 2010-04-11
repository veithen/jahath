package com.google.code.jahath.gateway.direct;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.google.code.jahath.Connection;
import com.google.code.jahath.Gateway;
import com.google.code.jahath.common.connection.SocketConnection;

public class DirectGateway implements Gateway {
    public Connection connect(InetSocketAddress socketAddress) throws IOException {
        return new SocketConnection(new Socket(socketAddress.getAddress(), socketAddress.getPort()));
    }
}
