package alatoo.edu.edtechmentorshipplatform.dto.chat;

import alatoo.edu.edtechmentorshipplatform.enums.MessageStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageResponseDto {
    
    private UUID id;
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private LocalDateTime sentAt;
    private MessageStatus status;
    private boolean isImportant;
    private String messageType; // Text, Image, File, etc.
}
