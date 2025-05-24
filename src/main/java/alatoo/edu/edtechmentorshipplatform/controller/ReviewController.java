package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.controller.base.BaseRestController;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import alatoo.edu.edtechmentorshipplatform.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "APIs for rating and feedback after sessions")
public class ReviewController extends BaseRestController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Leave a review for a session")
    public ResponseApi<ReviewResponseDto> leaveReview(
            @Valid @RequestBody ReviewRequestDto dto) {
        ReviewResponseDto result = reviewService.leaveReview(dto);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @GetMapping("/reviewee/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all reviews for a user")
    public ResponseApi<List<ReviewResponseDto>> getReviewsForUser(
            @PathVariable UUID userId) {
        List<ReviewResponseDto> result = reviewService.getReviewsForUser(userId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @GetMapping("/reviewer/{reviewerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all reviews made by a reviewer")
    public ResponseApi<List<ReviewResponseDto>> getReviewsByReviewer(
            @PathVariable UUID reviewerId) {
        List<ReviewResponseDto> result = reviewService.getReviewsByReviewer(reviewerId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }
}
