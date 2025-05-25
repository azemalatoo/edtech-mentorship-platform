package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.Review;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDto dto, User reviewer, User reviewee, MentorshipSession session) {
        return Review.builder()
                .session(session)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .rating(dto.getRating())
                .comments(dto.getComments())
                .build();
    }

    public ReviewResponseDto toDto(Review entity) {
        return ReviewResponseDto.builder()
                .id(entity.getId())
                .sessionId(entity.getSession().getId())
                .reviewerId(entity.getReviewer().getId())
                .revieweeId(entity.getReviewee().getId())
                .rating(entity.getRating())
                .comments(entity.getComments())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

