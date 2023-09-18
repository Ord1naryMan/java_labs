package org.labs.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import static org.labs.client.MessageHandler.send;

public class Main {

    public static boolean isRuning = true;

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 9090);
        ObjectOutputStream sendStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream receiveStream = new ObjectInputStream(socket.getInputStream());
        MessageHandler.sendStream = sendStream;
        MessageHandler.receiveStream = receiveStream;

        Authentication.register("test", "test".getBytes());
        Thread.sleep(100);
        Authentication.login("test", "test1".getBytes());
        Authentication.login("test", "test".getBytes());
        MessageHandler.send("Hello world");
        Authentication.disconnect();

        Thread.sleep(1000);
        isRuning = false;

        sendStream.close();
        receiveStream.close();
        socket.close();
    }
}
