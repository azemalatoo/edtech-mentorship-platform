package alatoo.edu.edtechmentorshipplatform.controller;


import alatoo.edu.edtechmentorshipplatform.dto.chat.ConversationResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@Api(value = "Messaging/Chat", tags = "Messaging/Chat Operations")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "Create a conversation between mentor and mentee")
    @PostMapping("/createConversation")
    public ConversationResponseDto createConversation(@RequestParam UUID mentorId, @RequestParam UUID menteeId, @RequestParam String conversationType) {
        return messageService.createConversation(mentorId, menteeId, conversationType);
    }

    @ApiOperation(value = "Send a message in an existing conversation")
    @PostMapping("/sendMessage")
    public MessageResponseDto sendMessage(@RequestParam Long conversationId, @RequestParam UUID senderId, @RequestParam UUID recipientId, @RequestBody MessageRequestDto messageRequestDto) {
        return messageService.sendMessage(conversationId, senderId, recipientId, messageRequestDto);
    }

    @ApiOperation(value = "Get all messages in a conversation")
    @GetMapping("/getMessages/{conversationId}")
    public List<MessageResponseDto> getMessages(@PathVariable Long conversationId) {
        return messageService.getMessagesByConversation(conversationId);
    }

    @ApiOperation(value = "Mark message as read")
    @PostMapping("/markAsRead/{messageId}")
    public void markMessageAsRead(@PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
    }

    @ApiOperation(value = "Get all conversations for a user")
    @GetMapping("/getConversations/{userId}")
    public List<ConversationResponseDto> getConversations(@PathVariable UUID userId) {
        return messageService.getConversationsByUser(userId);
    }
}
