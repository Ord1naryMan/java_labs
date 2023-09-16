package org.labs.server;

import java.io.IOException;

public class MessageHandler extends Thread {

    @Override
    public void run() {
        while (Main.isRunning) {
            for (var user : Main.connectionsMapping.entrySet()) {
                try {
                    if (user.getValue().receiveStream.available() != 0) {
                        //skip one byte 'cause in java there is no non-blocking
                        //object stream, so client sends some byte
                        //to indicate that he want to send something
                        user.getValue().receiveStream.skipBytes(1);
                        String action = (String) user.getValue()
                                .receiveStream
                                .readObject();
                        handleAction(user.getValue(), action);
                    }
                } catch (IOException ignored) {
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("something wrong have been sent");
                }
            }
        }
    }

    private void handleAction(Connections connections, String action) throws IOException, ClassNotFoundException {
        switch (action) {
            case "register:" -> {
                String login = (String) connections.receiveStream.readObject();
                byte[] pass = (byte[]) connections.receiveStream.readObject();
                System.out.println(login + " " + new String(pass));
            }
        }
    }
}
