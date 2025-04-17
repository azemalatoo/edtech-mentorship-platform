package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Review;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.User;

public class ReviewMapper {

    public static Review toEntity(ReviewRequestDto dto, User reviewer, User reviewee, MentorshipSession session) {
        return Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .session(session)
                .rating(dto.getRating())
                .comments(dto.getComments())
                .build();
    }

    public static ReviewResponseDto toDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setSessionId(review.getSession().getId());
        dto.setReviewerId(review.getReviewer().getId());
        dto.setRevieweeId(review.getReviewee().getId());
        dto.setRating(review.getRating());
        dto.setComments(review.getComments());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
