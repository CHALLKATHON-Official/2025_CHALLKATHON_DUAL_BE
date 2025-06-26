package kr.klr.challkathon.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/room/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/addUser/{roomId}")
    @SendTo("/room/{roomId}")
    public ChatMessage addUser(@DestinationVariable String roomId, ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        return chatMessage;
    }

    @GetMapping("/chat/{roomId}")
    public String getChatRoom(@PathVariable String roomId) {
        return "chat";
    }
}