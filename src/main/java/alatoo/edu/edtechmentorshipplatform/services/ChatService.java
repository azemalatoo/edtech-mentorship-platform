package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ChatMessage;

public interface ChatService {
    ChatMessage sendMessage(ChatMessage message);
    ChatMessage readReceipt(Long messageId);
}