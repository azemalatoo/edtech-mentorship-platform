package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ConversationResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageResponseDto;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import alatoo.edu.edtechmentorshipplatform.services.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@Api(value = "Messaging/Chat", tags = "Messaging/Chat Operations")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "Create a conversation between mentor and mentee")
    @PostMapping("/createConversation")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<ConversationResponseDto> createConversation(
            @RequestParam UUID mentorId,
            @RequestParam UUID menteeId,
            @RequestParam String conversationType) {
        ConversationResponseDto result =
                messageService.createConversation(mentorId, menteeId, conversationType);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @ApiOperation(value = "Send a message in an existing conversation")
    @PostMapping("/sendMessage")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<MessageResponseDto> sendMessage(
            @RequestParam Long conversationId,
            @RequestParam UUID senderId,
            @RequestParam UUID recipientId,
            @Valid @RequestBody MessageRequestDto messageRequestDto) {
        MessageResponseDto result =
                messageService.sendMessage(conversationId, senderId, recipientId, messageRequestDto);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @ApiOperation(value = "Get all messages in a conversation")
    @GetMapping("/getMessages/{conversationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<List<MessageResponseDto>> getMessages(
            @PathVariable Long conversationId) {
        List<MessageResponseDto> result =
                messageService.getMessagesByConversation(conversationId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @ApiOperation(value = "Mark message as read")
    @PostMapping("/markAsRead/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<Void> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markMessageAsRead(messageId);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }

    @ApiOperation(value = "Get all conversations for a user")
    @GetMapping("/getConversations/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<List<ConversationResponseDto>> getConversations(@PathVariable UUID userId) {
        List<ConversationResponseDto> result =
                messageService.getConversationsByUser(userId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }
}
