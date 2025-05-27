package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.review.ReviewResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.Review;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewMapperTest {

    private final ReviewMapper mapper = new ReviewMapper();

    @Test
    void toEntity_shouldMapAllFields() {
        // given
        ReviewRequestDto dto = new ReviewRequestDto();
        dto.setRating(4);
        dto.setComments("Great session");

        User reviewer = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000A")).build();
        User reviewee = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000B")).build();
        MentorshipSession session = MentorshipSession.builder().id(55L).build();

        // when
        Review entity = mapper.toEntity(dto, reviewer, reviewee, session);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getSession()).isSameAs(session);
        assertThat(entity.getReviewer()).isSameAs(reviewer);
        assertThat(entity.getReviewee()).isSameAs(reviewee);
        assertThat(entity.getRating()).isEqualTo(4);
        assertThat(entity.getComments()).isEqualTo("Great session");
    }

    @Test
    void toEntity_nullDto_shouldThrowNpe() {
        User reviewer = User.builder().id(UUID.randomUUID()).build();
        User reviewee = User.builder().id(UUID.randomUUID()).build();
        MentorshipSession session = MentorshipSession.builder().id(1L).build();
        assertThrows(NullPointerException.class,
            () -> mapper.toEntity(null, reviewer, reviewee, session));
    }

    @Test
    void toDto_fullEntity_shouldMapAllFields() {
        // given
        User reviewer = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000C")).build();
        User reviewee = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000D")).build();
        MentorshipSession session = MentorshipSession.builder().id(99L).build();
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 27, 12, 0);

        Review entity = Review.builder()
            .session(session)
            .reviewer(reviewer)
            .reviewee(reviewee)
            .rating(5)
            .comments("Excellent")
            .build();
        entity.setId(77L);
        entity.setCreatedAt(createdAt);

        // when
        ReviewResponseDto dto = mapper.toDto(entity);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(77L);
        assertThat(dto.getSessionId()).isEqualTo(99L);
        assertThat(dto.getReviewerId()).isEqualTo(reviewer.getId());
        assertThat(dto.getRevieweeId()).isEqualTo(reviewee.getId());
        assertThat(dto.getRating()).isEqualTo(5);
        assertThat(dto.getComments()).isEqualTo("Excellent");
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void toDto_nullEntity_shouldThrowNpe() {
        assertThrows(NullPointerException.class,
            () -> mapper.toDto(null));
    }
}
