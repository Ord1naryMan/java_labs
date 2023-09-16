package org.labs.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MessageHandler {

    public static ObjectOutputStream sendStream;
    public static ObjectInputStream receiveStream;


    public static Message send(Object object) throws IOException {
        //we want to send some byte so server knows that we want to send something
        sendStream.writeByte(0);
        sendStream.writeObject(object);
        return new Message();
    }

    public static class Message {
        public Message also(Object o) throws IOException {
            sendStream.writeObject(o);
            return this;
        }
    }
}
