package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import java.util.List;
import java.util.UUID;

public interface ConversationService {
    Conversation createConversation(UUID mentorId, UUID menteeId);
    List<Conversation> getConversationsForUser(UUID userId);
    Conversation closeConversation(Long conversationId);
}