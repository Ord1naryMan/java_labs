package org.labs.client;

import org.labs.server.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MessageHandler extends Thread {

    public static ObjectOutputStream sendStream;
    public static ObjectInputStream receiveStream;

    @Override
    public void run() {
        while (org.labs.server.Main.isRunning) {
            try {
                if (receiveStream.available() != 0) {
                    receiveStream.skipBytes(1);
                    String message = (String) receiveStream.readObject();
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("some IOException has occurred MessageHandler.class line 23");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("something wrong have been sent");
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
