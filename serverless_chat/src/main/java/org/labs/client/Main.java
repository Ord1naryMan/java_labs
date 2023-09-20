package org.labs.client;

import org.labs.server.ConnectionHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static final Map<Integer, Connections> connectionsMapping = new ConcurrentHashMap<>();
    public static final Map<String, Connections> users = new ConcurrentHashMap<>();
    public static boolean isRunning = true;
    public static ServerSocket server;

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 9090);


        server = new ServerSocket(Integer.parseInt(args[1]));

        ConnectionHandler connectionHandler = new ConnectionHandler();
        connectionHandler.start();

        MessageHandler messageHandler = new MessageHandler();
        messageHandler.start();

        System.out.println("trying to register");


        Authentication.register("test", "test".getBytes());
        System.out.println("registered");
        Thread.sleep(100);
        System.out.println("trying to login with incorrect password");
        Authentication.login("test", "test1".getBytes());
        System.out.println("trying to login with correct password");
        Authentication.login("test", "test".getBytes());
        System.out.println("trying to send a message");
        MessageHandler.find("test");
        MessageHandler.sendMessageTo("test", "Hello world");
        Thread.sleep(10000);
        System.out.println("disconnecting");
        Authentication.disconnect();

        isRunning = false;

        server.close();

        connectionHandler.join();
        messageHandler.join();

        for (var connection : connectionsMapping.entrySet()) {
            Connections connectionValue = connection.getValue();
            connectionValue.receiveStream.close();
            connectionValue.sendStream.close();
            connectionValue.socket.close();
        }
    }
}
