package alatoo.edu.edtechmentorshipplatform.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request to submit a review for a completed session.
 */
@Data
public class ReviewRequestDto {
    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @Size(max = 1000, message = "Comments too long")
    private String comments;
}
