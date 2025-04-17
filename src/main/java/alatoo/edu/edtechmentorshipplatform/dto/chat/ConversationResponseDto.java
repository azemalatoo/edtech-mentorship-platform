package alatoo.edu.edtechmentorshipplatform.dto.chat;

import alatoo.edu.edtechmentorshipplatform.enums.ConversationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ConversationResponseDto {

    private UUID id;
    private UUID mentorId;
    private UUID menteeId;
    private String conversationType;
    private LocalDateTime createdAt;
    private LocalDateTime lastActiveAt;
    private Boolean isActive;
    private ConversationStatus status;
}
