package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ConversationResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.User;

import java.time.LocalDateTime;

public class ConversationMapper {

    public static Conversation toEntity(ConversationResponseDto dto, User mentor, User mentee) {
        return Conversation.builder()
                .id(dto.getId())
                .mentor(mentor)
                .mentee(mentee)
                .conversationType(dto.getConversationType())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .lastActiveAt(dto.getLastActiveAt())
                .isActive(dto.getIsActive())
                .status(dto.getStatus())
                .build();
    }

    public static ConversationResponseDto toDto(Conversation entity) {
        return ConversationResponseDto.builder()
                .id(entity.getId())
                .mentorId(entity.getMentor().getId())
                .menteeId(entity.getMentee().getId())
                .conversationType(entity.getConversationType())
                .createdAt(entity.getCreatedAt())
                .lastActiveAt(entity.getLastActiveAt())
                .isActive(entity.getIsActive())
                .status(entity.getStatus())
                .build();
    }
}
