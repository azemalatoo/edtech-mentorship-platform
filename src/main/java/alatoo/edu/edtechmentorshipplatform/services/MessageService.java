package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.chat.ConversationResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageResponseDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    ConversationResponseDto createConversation(UUID mentorId, UUID menteeId, String conversationType);
    MessageResponseDto sendMessage(UUID conversationId, UUID senderId, UUID recipientId, MessageRequestDto messageRequestDto);
    List<MessageResponseDto> getMessagesByConversation(UUID conversationId);
    void markMessageAsRead(UUID messageId);
    List<ConversationResponseDto> getConversationsByUser(UUID userId);
}
