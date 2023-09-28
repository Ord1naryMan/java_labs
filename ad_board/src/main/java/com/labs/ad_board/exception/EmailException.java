package com.labs.ad_board.exception;

public class EmailException extends RuntimeException {

    public EmailException() {
        super();
    }

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Exception e) {
        super(message, e);
    }

    public EmailException(Exception e) {
        super(e);
    }
}
