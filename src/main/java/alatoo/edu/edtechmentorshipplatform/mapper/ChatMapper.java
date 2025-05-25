package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ChatMessage;
import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.Message;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {
    public Message toEntity(ChatMessage dto, Conversation conv, User sender, User recipient) {
        return Message.builder()
                .conversation(conv)
                .sender(sender)
                .recipient(recipient)
                .content(dto.getContent())
                .attachmentUrl(dto.getAttachmentUrl())
                .isImportant(false)
                .messageType(dto.getAttachmentUrl() != null ? "ATTACHMENT" : "TEXT")
                .build();
    }

    public ChatMessage toDto(Message entity) {
        ChatMessage dto = new ChatMessage();
        dto.setId(entity.getId());
        dto.setConversationId(entity.getConversation().getId());
        dto.setSenderId(entity.getSender().getId());
        dto.setRecipientId(entity.getRecipient().getId());
        dto.setContent(entity.getContent());
        dto.setAttachmentUrl(entity.getAttachmentUrl());
        dto.setStatus(entity.getStatus());
        dto.setSentAt(entity.getSentAt());
        return dto;
    }
}
