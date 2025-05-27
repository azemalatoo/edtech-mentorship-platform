package alatoo.edu.edtechmentorshipplatform.services;

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
import alatoo.edu.edtechmentorshipplatform.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock private ReviewRepo reviewRepo;
    @Mock private MentorshipSessionRepo sessionRepo;
    @Mock private ReviewMapper mapper;

    @InjectMocks
    private ReviewServiceImpl service;

    private User mentor;
    private User mentee;
    private MentorshipSession session;
    private SecurityContext originalContext;

    @BeforeEach
    void setUp() {
        originalContext = SecurityContextHolder.getContext();

        mentor = User.builder().id(UUID.randomUUID()).build();
        mentee = User.builder().id(UUID.randomUUID()).build();

        session = MentorshipSession.builder()
                .id(123L)
                .mentor(mentor)
                .mentee(mentee)
                .status(SessionStatus.COMPLETED)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.setContext(originalContext);
    }

    private void authenticateAs(User u) {
        var uds = new UserDetailsImpl(u);
        var auth = new TestingAuthenticationToken(uds, null);
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }

    @Test
    void whenSessionNotFound_thenThrowNotFoundException() {
        when(sessionRepo.findById(999L)).thenReturn(Optional.empty());

        var dto = new ReviewRequestDto();
        dto.setSessionId(999L);
        dto.setRating(5);

        assertThatThrownBy(() -> service.submitReview(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Session not found with id 999");
    }

    @Test
    void whenSessionNotCompleted_thenThrowIllegalStateException() {
        session.setStatus(SessionStatus.AVAILABLE);
        when(sessionRepo.findById(123L)).thenReturn(Optional.of(session));

        var dto = new ReviewRequestDto();
        dto.setSessionId(123L);
        dto.setRating(4);

        assertThatThrownBy(() -> service.submitReview(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Can only review completed sessions");
    }

    @Test
    void whenUserNotParticipant_thenThrowAccessDenied() {
        when(sessionRepo.findById(123L)).thenReturn(Optional.of(session));
        authenticateAs(User.builder().id(UUID.randomUUID()).build());

        var dto = new ReviewRequestDto();
        dto.setSessionId(123L);
        dto.setRating(3);

        assertThatThrownBy(() -> service.submitReview(dto))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Only session participants can review");
    }

    @Test
    void whenReviewAlreadyExists_thenThrowReviewAlreadyExistsException() {
        when(sessionRepo.findById(123L)).thenReturn(Optional.of(session));
        authenticateAs(mentor);

        when(reviewRepo.findBySessionIdAndReviewerId(123L, mentor.getId()))
                .thenReturn(Optional.of(new Review()));

        var dto = new ReviewRequestDto();
        dto.setSessionId(123L);
        dto.setRating(5);

        assertThatThrownBy(() -> service.submitReview(dto))
                .isInstanceOf(ReviewAlreadyExistsException.class)
                .hasMessage("Review already exists for session 123");
    }

    @Test
    void whenSubmitReviewWithValidRequest_thenSaveAndReturnDto() {
        when(sessionRepo.findById(123L)).thenReturn(Optional.of(session));
        authenticateAs(mentee);

        when(reviewRepo.findBySessionIdAndReviewerId(123L, mentee.getId()))
                .thenReturn(Optional.empty());

        var dto = new ReviewRequestDto();
        dto.setSessionId(123L);
        dto.setRating(4);
        dto.setComments("Great!");

        var toSave = new Review();
        when(mapper.toEntity(dto, mentee, mentor, session)).thenReturn(toSave);

        var saved = new Review();
        saved.setId(77L);
        when(reviewRepo.save(toSave)).thenReturn(saved);

        var resp = new ReviewResponseDto();
        resp.setId(77L);
        when(mapper.toDto(saved)).thenReturn(resp);

        var result = service.submitReview(dto);

        verify(reviewRepo).save(toSave);
        assertThat(result).isSameAs(resp);
    }

    @Test
    void getReviewsForUser_returnsMappedDtos() {
        UUID rid = UUID.randomUUID();
        var r1 = new Review(); r1.setId(1L);
        var r2 = new Review(); r2.setId(2L);
        when(reviewRepo.findAllByRevieweeId(rid)).thenReturn(List.of(r1, r2));

        var d1 = new ReviewResponseDto(); d1.setId(1L);
        var d2 = new ReviewResponseDto(); d2.setId(2L);
        when(mapper.toDto(r1)).thenReturn(d1);
        when(mapper.toDto(r2)).thenReturn(d2);

        var out = service.getReviewsForUser(rid);
        assertThat(out).containsExactly(d1, d2);
    }

    @Test
    void whenDeletingNonexistentReview_thenThrowNotFoundException() {
        when(reviewRepo.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteReview(10L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Review not found with id 10");
    }

    @Test
    void whenDeletingByNonReviewer_thenThrowAccessDenied() {
        var rev = new Review();
        rev.setId(5L);
        rev.setReviewer(mentor);

        when(reviewRepo.findById(5L)).thenReturn(Optional.of(rev));
        authenticateAs(mentee);

        assertThatThrownBy(() -> service.deleteReview(5L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Only the original reviewer can delete this review");
    }

    @Test
    void whenDeletingByOriginalReviewer_thenDeleteReview() {
        var rev = new Review();
        rev.setId(8L);
        rev.setReviewer(mentee);

        when(reviewRepo.findById(8L)).thenReturn(Optional.of(rev));
        authenticateAs(mentee);

        service.deleteReview(8L);
        verify(reviewRepo).delete(rev);
    }
}
