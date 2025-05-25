package alatoo.edu.edtechmentorshipplatform.dto.chat;

import alatoo.edu.edtechmentorshipplatform.enums.MessageStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatMessage {
    private Long id;
    private Long conversationId;
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private String attachmentUrl;
    private MessageStatus status;
    private LocalDateTime sentAt;
}