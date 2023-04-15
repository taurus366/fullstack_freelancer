package com.fullstack.freelancer.model.dto;

public class ChatMessage {

    private String sender;
    private String content;


    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public ChatMessage setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ChatMessage setContent(String content) {
        this.content = content;
        return this;
    }
}
