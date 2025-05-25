package alatoo.edu.edtechmentorshipplatform.dto.session;

import alatoo.edu.edtechmentorshipplatform.enums.AccessLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SessionRecordingRequestDto {
    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Recording URL is required")
    @Size(max = 1000, message = "Recording URL too long")
    private String recordingUrl;

    @NotNull(message = "Access level is required")
    private AccessLevel accessLevel;
}
