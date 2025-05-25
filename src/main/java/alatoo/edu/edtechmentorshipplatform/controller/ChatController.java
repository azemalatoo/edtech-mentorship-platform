package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ChatMessage;
import alatoo.edu.edtechmentorshipplatform.services.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void handleSend(@Payload ChatMessage message) {
        chatService.sendMessage(message);
    }

    @MessageMapping("/chat.read")
    public void handleRead(@Payload ChatMessage message) {
        chatService.readReceipt(message.getId());
    }
}