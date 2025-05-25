package alatoo.edu.edtechmentorshipplatform.dto.session;

import alatoo.edu.edtechmentorshipplatform.enums.SessionProviderType;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MentorshipSessionResponseDto {
    private Long id;
    private UUID mentorId;
    private UUID menteeId;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private SessionStatus status;
    private SessionProviderType providerType;
    private String meetingLink;
    private String notes;
}