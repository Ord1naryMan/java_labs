package org.labs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static final Map<String, String> privateKeys = new ConcurrentHashMap<>();
    public static final Map<Integer, Connections> connectionsMapping = new ConcurrentHashMap<>();
    public static final Map<String, String> loginPassword = new ConcurrentHashMap<>();
    public static final Map<String, Integer> loginPort = new ConcurrentHashMap<>();
    public static final boolean isRunning = true;
    public static ServerSocket server;

    public static void main(String[] args) throws Exception {
        server = new ServerSocket(9090);
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connectionHandler.start();
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.start();

        System.out.println("Server has been initialised");

        connectionHandler.join();
        messageHandler.join();
        for (var connection : connectionsMapping.entrySet()) {
            Connections connectionValue = connection.getValue();
            connectionValue.receiveStream.close();
            connectionValue.sendStream.close();
            connectionValue.socket.close();
        }
        server.close();
    }
}
