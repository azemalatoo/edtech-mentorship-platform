package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReviewService {
    ReviewResponseDto leaveReview(ReviewRequestDto dto);
    List<ReviewResponseDto> getReviewsForUser(UUID userId);
    List<ReviewResponseDto> getReviewsByReviewer(UUID reviewerId);
}
