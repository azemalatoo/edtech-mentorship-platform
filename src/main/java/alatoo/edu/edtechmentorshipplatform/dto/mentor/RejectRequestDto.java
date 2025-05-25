package alatoo.edu.edtechmentorshipplatform.dto.mentor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for providing a rejection reason when an admin rejects a mentor profile.
 */
@Data
public class RejectRequestDto {
    @NotBlank(message = "Rejection reason is required")
    private String reason;
}