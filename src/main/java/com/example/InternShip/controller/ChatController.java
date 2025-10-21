package com.example.InternShip.controller;

import com.example.InternShip.dto.request.ChatMessageRequest;
import com.example.InternShip.dto.response.ChatMessageResponse;
import com.example.InternShip.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest chatMessageRequest, Principal principal) {
        // Save the message to the database
        ChatMessageResponse savedMessage = chatService.saveMessage(chatMessageRequest, principal.getName());

        // Broadcast the message to the specific conversation topic
        String destination = "/topic/conversation/" + savedMessage.getConversationId();
        messagingTemplate.convertAndSend(destination, savedMessage);
    }

}
