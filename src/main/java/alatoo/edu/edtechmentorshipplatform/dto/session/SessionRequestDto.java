package alatoo.edu.edtechmentorshipplatform.dto.session;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SessionRequestDto {

    @NotNull(message = "Mentor ID must not be null")
    private UUID mentorId;

    @NotNull(message = "Mentee ID must not be null")
    private UUID menteeId;

    @NotNull(message = "Scheduled date and time must not be null")
    private LocalDateTime scheduledAt;

    private UUID tutoringPackageId;

    private String notes;
}
