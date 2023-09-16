package org.labs.client;

import java.io.IOException;

import static org.labs.client.MessageHandler.send;

public class Authentication {

    public static void register(String login, byte[] password) {
        try {
            send("register:")
                .also(login)
                .also(password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
