package alatoo.edu.edtechmentorshipplatform.dto.session;

import alatoo.edu.edtechmentorshipplatform.enums.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionRecordingResponseDto {
    private Long id;
    private Long sessionId;
    private String recordingUrl;
    private LocalDateTime uploadedAt;
    private AccessLevel accessLevel;
}
