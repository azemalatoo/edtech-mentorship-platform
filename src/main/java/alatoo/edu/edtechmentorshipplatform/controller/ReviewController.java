package alatoo.edu.edtechmentorshipplatform.controller;


import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "APIs for rating and feedback after sessions")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Leave a review for a session")
    public ReviewResponseDto leaveReview(@RequestBody @Valid ReviewRequestDto dto) {
        return reviewService.leaveReview(dto);
    }

    @GetMapping("/reviewee/{userId}")
    @Operation(summary = "Get all reviews for a user")
    public List<ReviewResponseDto> getReviewsForUser(@PathVariable UUID userId) {
        return reviewService.getReviewsForUser(userId);
    }

    @GetMapping("/reviewer/{reviewerId}")
    @Operation(summary = "Get all reviews made by a reviewer")
    public List<ReviewResponseDto> getReviewsByReviewer(@PathVariable UUID reviewerId) {
        return reviewService.getReviewsByReviewer(reviewerId);
    }
}
