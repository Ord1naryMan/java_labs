package org.labs.client;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.labs.client.MessageHandler.send;

public class Authentication {
    public static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest
                    .getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(String login, byte[] password) {
        String hashedPasswd =
                new String(messageDigest.digest(password));

        Arrays.fill(password, (byte) ' ');
        send("register:")
                .also(login)
                .also(hashedPasswd);
    }

    public static void disconnect() {
        send("disconnect:");
    }

    public static void login(String login, byte[] passwd) {
        String hashedPasswd =
                new String(messageDigest.digest(passwd));

        Arrays.fill(passwd, (byte) ' ');
        send("login:")
                .also(login)
                .also(hashedPasswd);

        String answer = MessageHandler.receiveString();
        if (answer.equals("invalid")) {
            System.out.println("Invalid password!");
            //TODO show user error
        }
    }
}
