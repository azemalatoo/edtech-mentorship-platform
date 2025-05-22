package alatoo.edu.edtechmentorshipplatform.dto.session;

import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SessionResponseDto {

    private Long id;
    private UUID mentorId;
    private UUID menteeId;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private SessionStatus status;
    private String notes;
    private boolean isCompleted;
    private Long tutoringPackageId;
}
