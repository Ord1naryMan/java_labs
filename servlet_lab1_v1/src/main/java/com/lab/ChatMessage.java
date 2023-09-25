package com.lab;


public class ChatMessage {
    // Текст сообщения
    private String message;
    // Автор сообщения
    private ChatUser author;
    // Временная метка сообщения (в микросекундах)
    private long timestamp;

    private ChatUser onlyFor = null;

    public ChatMessage(String message, ChatUser author, long timestamp, ChatUser onlyFor) {
        super();
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
        this.onlyFor = onlyFor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatUser getAuthor() {
        return author;
    }

    public void setAuthor(ChatUser author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ChatUser getOnlyFor() {
        return onlyFor;
    }

    public void setOnlyFor(ChatUser onlyFor) {
        this.onlyFor = onlyFor;
    }
}
