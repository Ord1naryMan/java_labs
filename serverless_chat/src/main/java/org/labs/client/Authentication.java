package org.labs.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.labs.client.MessageHandler.send;

public class Authentication {

    public static final Map<String, Connections> users = new ConcurrentHashMap<>();

    public static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest
                    .getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String register(String login, byte[] password) {
        String hashedPasswd =
                new String(messageDigest.digest(password));

        Arrays.fill(password, (byte) ' ');
        send("register:")
                .also(login)
                .also(hashedPasswd);

        return MessageHandler.receiveString();
    }

    public static void disconnect() {
        send("disconnect:");
    }

    public static String login(String login, byte[] passwd) {
        String hashedPasswd =
                new String(messageDigest.digest(passwd));

        Arrays.fill(passwd, (byte) ' ');
        send("login:")
                .also(login)
                .also(hashedPasswd);


        String answer = MessageHandler.receiveString();
        if (answer.equals("success")) {
            ConnectionHandler.updateUsers();
        }

        return answer;
    }
}
