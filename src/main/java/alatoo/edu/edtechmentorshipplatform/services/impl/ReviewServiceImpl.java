package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.Review;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.ReviewMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MentorshipSessionRepo;
import alatoo.edu.edtechmentorshipplatform.repo.ReviewRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepository;
    private final UserRepo userRepository;
    private final MentorshipSessionRepo sessionRepository;

    @Override
    public ReviewResponseDto leaveReview(ReviewRequestDto dto) {
        User reviewer = userRepository.findById(dto.getReviewerId())
                .orElseThrow(() -> new NotFoundException("Reviewer not found"));
        User reviewee = userRepository.findById(dto.getRevieweeId())
                .orElseThrow(() -> new NotFoundException("Reviewee not found"));
        MentorshipSession session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new NotFoundException("Session not found"));

        Review review = ReviewMapper.toEntity(dto, reviewer, reviewee, session);
        return ReviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponseDto> getReviewsForUser(UUID userId) {
        return reviewRepository.findByRevieweeId(userId)
                .stream()
                .map(ReviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDto> getReviewsByReviewer(UUID reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId)
                .stream()
                .map(ReviewMapper::toDto)
                .collect(Collectors.toList());
    }
}
