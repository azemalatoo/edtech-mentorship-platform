package alatoo.edu.edtechmentorshipplatform.dto.session;

import alatoo.edu.edtechmentorshipplatform.enums.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SessionRecordingResponseDto {
    private Long id;
    private Long sessionId;
    private String recordingUrl;
    private LocalDateTime uploadedAt;
    private AccessLevel accessLevel;
}
