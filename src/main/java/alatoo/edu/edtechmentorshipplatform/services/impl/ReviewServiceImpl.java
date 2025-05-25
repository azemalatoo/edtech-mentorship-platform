package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.Review;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.exception.ReviewAlreadyExistsException;
import alatoo.edu.edtechmentorshipplatform.mapper.ReviewMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MentorshipSessionRepo;
import alatoo.edu.edtechmentorshipplatform.repo.ReviewRepo;
import alatoo.edu.edtechmentorshipplatform.security.UserDetailsImpl;
import alatoo.edu.edtechmentorshipplatform.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepo reviewRepo;
    private final MentorshipSessionRepo sessionRepo;
    private final ReviewMapper mapper;

    private UUID getCurrentUserId() {
        return ((UserDetailsImpl)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        ).getUser().getId();
    }

    @Override
    public ReviewResponseDto submitReview(ReviewRequestDto dto) {
        MentorshipSession session = sessionRepo.findById(dto.getSessionId())
                .orElseThrow(() -> new NotFoundException(
                        "Session not found with id " + dto.getSessionId()
                ));

        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalStateException("Can only review completed sessions");
        }

        UUID reviewerId = getCurrentUserId();
        User reviewer = session.getMentor().getId().equals(reviewerId)
                ? session.getMentor()
                : session.getMentee().getId().equals(reviewerId)
                ? session.getMentee()
                : null;
        if (reviewer == null) {
            throw new AccessDeniedException("Only session participants can review");
        }

        User reviewee = reviewer.equals(session.getMentor())
                ? session.getMentee()
                : session.getMentor();

        reviewRepo.findBySessionIdAndReviewerId(session.getId(), reviewerId)
                .ifPresent(r -> { throw new ReviewAlreadyExistsException(
                        "Review already exists for session " + session.getId()
                ); });

        Review saved = reviewRepo.save(
                mapper.toEntity(dto, reviewer, reviewee, session)
        );
        return mapper.toDto(saved);
    }

    @Override
    public List<ReviewResponseDto> getReviewsForUser(UUID revieweeId) {
        return reviewRepo.findAllByRevieweeId(revieweeId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(
                        "Review not found with id " + reviewId
                ));

        UUID userId = getCurrentUserId();
        if (!review.getReviewer().getId().equals(userId)) {
            throw new AccessDeniedException("Only the original reviewer can delete this review");
        }

        reviewRepo.delete(review);
    }
}
