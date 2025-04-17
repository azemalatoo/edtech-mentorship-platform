package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Message;
import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.User;

import java.time.LocalDateTime;

public class MessageMapper {

    public static Message toEntity(MessageResponseDto dto, Conversation conversation, User sender, User recipient) {
        return Message.builder()
                .id(dto.getId())
                .conversation(conversation)
                .sender(sender)
                .recipient(recipient)
                .content(dto.getContent())
                .sentAt(dto.getSentAt() != null ? dto.getSentAt() : LocalDateTime.now())
                .status(dto.getStatus())
                .isImportant(dto.isImportant())
                .messageType(dto.getMessageType())
                .build();
    }

    public static MessageResponseDto toDto(Message entity) {
        return MessageResponseDto.builder()
                .id(entity.getId())
                .senderId(entity.getSender().getId())
                .recipientId(entity.getRecipient().getId())
                .content(entity.getContent())
                .sentAt(entity.getSentAt())
                .status(entity.getStatus())
                .isImportant(entity.isImportant())
                .messageType(entity.getMessageType())
                .build();
    }
}
