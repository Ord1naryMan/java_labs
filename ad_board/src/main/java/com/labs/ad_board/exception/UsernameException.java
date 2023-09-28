package com.labs.ad_board.exception;

public class UsernameException extends RuntimeException {

    public UsernameException() {
        super();
    }

    public UsernameException(String message) {
        super(message);
    }

    public UsernameException(String message, Exception e) {
        super(message, e);
    }

    public UsernameException(Exception e) {
        super(e);
    }
}
