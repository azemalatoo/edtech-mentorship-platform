package alatoo.edu.edtechmentorshipplatform.dto.review;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewResponseDto {
    private Long id;
    private Long sessionId;
    private UUID reviewerId;
    private UUID revieweeId;
    private int rating;
    private String comments;
    private LocalDateTime createdAt;
}
