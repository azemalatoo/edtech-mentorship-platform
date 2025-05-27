package alatoo.edu.edtechmentorshipplatform.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private Long sessionId;
    private UUID reviewerId;
    private UUID revieweeId;
    private int rating;
    private String comments;
    private LocalDateTime createdAt;
}