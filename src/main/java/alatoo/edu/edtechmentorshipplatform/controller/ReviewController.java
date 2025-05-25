package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.ReviewService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/reviews")
@Tag(name = "ReviewController", description = "APIs for managing session reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<ReviewResponseDto> submitReview(
            @Valid @RequestBody ReviewRequestDto request) {
        return new ResponseApi<>(
                reviewService.submitReview(request),
                ResponseCode.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<List<ReviewResponseDto>> getReviews(
            @RequestParam UUID revieweeId) {
        return new ResponseApi<>(
                reviewService.getReviewsForUser(revieweeId),
                ResponseCode.SUCCESS
        );
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}