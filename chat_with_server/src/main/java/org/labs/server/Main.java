package org.labs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static final Map<Integer, Connections> connectionsMapping = new ConcurrentHashMap<>();
    public static final Map<String, String> authPassword = new ConcurrentHashMap<>();
    public static final Map<String, Integer> loginPort = new ConcurrentHashMap<>();
    public static final Map<Integer, String> portLogin = new ConcurrentHashMap<>();
    public static boolean isRunning = true;
    public static ServerSocket server;


    public static void main(String[] args) throws Exception {
        server = new ServerSocket(9090);
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connectionHandler.start();
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.start();

        System.out.println("Server has been initialised");

        new Scanner(System.in).next();
        System.out.println("Press any button to exit");
        Main.isRunning = false;


        System.out.println("Exiting!!!");

        server.close();

        connectionHandler.join();
        messageHandler.join();


        System.out.println("Ending application:");
        System.out.println(authPassword);

        for (var connection : connectionsMapping.entrySet()) {
            Connections connectionValue = connection.getValue();
            connectionValue.receiveStream.close();
            connectionValue.sendStream.close();
            connectionValue.socket.close();
        }
    }
}
