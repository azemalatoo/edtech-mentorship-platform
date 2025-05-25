package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto submitReview(ReviewRequestDto requestDto);
    List<ReviewResponseDto> getReviewsForUser(java.util.UUID revieweeId);
    void deleteReview(Long reviewId);
}
