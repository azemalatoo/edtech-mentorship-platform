package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import java.util.List;

public interface ConversationService {
    Conversation createConversation(Long mentorId, Long menteeId);
    List<Conversation> getConversationsForUser(java.util.UUID userId);
    Conversation closeConversation(Long conversationId);
}