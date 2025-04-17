// ReviewRequestDto.java
package alatoo.edu.edtechmentorshipplatform.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewRequestDto {
    @NotNull
    private UUID sessionId;

    @NotNull
    private UUID reviewerId;

    @NotNull
    private UUID revieweeId;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String comments;
}
