package kr.klr.challkathon.global.controller;

import lombok.Data;

@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String roomId;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}