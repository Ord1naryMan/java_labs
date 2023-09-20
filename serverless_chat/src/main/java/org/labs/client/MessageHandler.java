package org.labs.client;

import org.labs.server.Main;
import org.labs.ui.ApplicationRunner;
import org.labs.ui.MessagingFrame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler extends Thread {

    public static ObjectOutputStream sendStream;
    public static ObjectInputStream receiveStream;

    @Override
    public void run() {
        while (ApplicationRunner.isRunning) {
            for (var user : Main.connectionsMapping.entrySet()) {
                try {
                    if (user.getValue().receiveStream.available() != 0) {
                        user.getValue().receiveStream.skipBytes(1);
                        String action = (String) user.getValue()
                                .receiveStream
                                .readObject();
                    }
                } catch (IOException e) {
                    System.out.println("some IOException has occurred MessageHandler.class line 23");
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("something wrong have been sent");
                }
            }
        }

    }


    public static Message send(Object object) {
        //we want to send some byte so server knows that we want to send something
        try {
            sendStream.writeByte(0);
            sendStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Message();
    }

    public static String find(String login) {
        try {
            sendStream.writeByte(0);
            sendStream.writeObject("find:");
            sendStream.writeObject(login);

            return (String) receiveStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessageTo(String login, String message) {
        try {
            sendStream.writeByte(0);
            sendStream.writeObject("send:");
            sendStream.writeObject(login);
            sendStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String receiveString() {
        try {
            return (String) receiveStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Message {
        public Message also(Object o) {
            try {
                sendStream.writeObject(o);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }
    }
}
