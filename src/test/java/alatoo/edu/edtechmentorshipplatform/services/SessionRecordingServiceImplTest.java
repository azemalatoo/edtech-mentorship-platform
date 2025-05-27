package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.SessionRecording;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.AccessLevel;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.SessionRecordingMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MentorshipSessionRepo;
import alatoo.edu.edtechmentorshipplatform.repo.SessionRecordingRepo;
import alatoo.edu.edtechmentorshipplatform.security.UserDetailsImpl;
import alatoo.edu.edtechmentorshipplatform.services.impl.SessionRecordingServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionRecordingServiceImplTest {
    @Mock private SessionRecordingRepo recordingRepo;
    @Mock private MentorshipSessionRepo sessionRepo;
    @Mock private SessionRecordingMapper mapper;

    @InjectMocks
    private SessionRecordingServiceImpl service;

    private User mentor;
    private User mentee;
    private MentorshipSession session;
    private TestingAuthenticationToken originalAuth;

    @BeforeEach
    void setUp() {
        originalAuth = (TestingAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        mentor = User.builder().id(UUID.randomUUID()).build();
        mentee = User.builder().id(UUID.randomUUID()).build();
        session = MentorshipSession.builder()
                .id(101L)
                .mentor(mentor)
                .mentee(mentee)
                .status(SessionStatus.COMPLETED)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.getContext().setAuthentication(originalAuth);
    }

    private void authenticateAs(User user) {
        var uds = new UserDetailsImpl(user);
        var auth = new TestingAuthenticationToken(uds, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void whenSessionNotFound_onUpload_thenThrowNotFoundException() {
        when(sessionRepo.findById(999L)).thenReturn(Optional.empty());

        var dto = new SessionRecordingRequestDto();
        dto.setSessionId(999L);
        dto.setRecordingUrl("http://rec");
        dto.setAccessLevel(AccessLevel.PRIVATE);

        authenticateAs(mentor);

        assertThatThrownBy(() -> service.uploadRecording(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Session not found with id 999");
    }

    @Test
    void whenSessionNotCompleted_onUpload_thenThrowIllegalStateException() {
        session.setStatus(SessionStatus.AVAILABLE);
        when(sessionRepo.findById(101L)).thenReturn(Optional.of(session));

        var dto = new SessionRecordingRequestDto();
        dto.setSessionId(101L);
        dto.setRecordingUrl("http://rec");
        dto.setAccessLevel(AccessLevel.PUBLIC);

        authenticateAs(mentor);

        assertThatThrownBy(() -> service.uploadRecording(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot upload recording: session not completed");
    }

    @Test
    void whenNonMentor_onUpload_thenThrowAccessDeniedException() {
        when(sessionRepo.findById(101L)).thenReturn(Optional.of(session));
        authenticateAs(mentee);

        var dto = new SessionRecordingRequestDto();
        dto.setSessionId(101L);
        dto.setRecordingUrl("http://rec");
        dto.setAccessLevel(AccessLevel.MENTEE);

        assertThatThrownBy(() -> service.uploadRecording(dto))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessage("Only the mentor can upload recordings");
    }

    @Test
    void whenMentorAndCompleted_onUpload_thenReturnDto() {
        when(sessionRepo.findById(101L)).thenReturn(Optional.of(session));
        authenticateAs(mentor);

        var dtoReq = new SessionRecordingRequestDto();
        dtoReq.setSessionId(101L);
        dtoReq.setRecordingUrl("http://rec");
        dtoReq.setAccessLevel(AccessLevel.MENTOR);

        var entity = SessionRecording.builder()
                .id(55L)
                .session(session)
                .recordingUrl("http://rec")
                .accessLevel(AccessLevel.MENTOR)
                .uploadedAt(LocalDateTime.now())
                .build();

        when(mapper.toEntity(dtoReq, session)).thenReturn(entity);
        when(recordingRepo.save(entity)).thenReturn(entity);

        var dtoResp = SessionRecordingResponseDto.builder()
                .id(55L)
                .sessionId(101L)
                .recordingUrl("http://rec")
                .accessLevel(AccessLevel.MENTOR)
                .uploadedAt(entity.getUploadedAt())
                .build();
        when(mapper.toDto(entity)).thenReturn(dtoResp);

        var result = service.uploadRecording(dtoReq);

        verify(recordingRepo).save(entity);
        assertThat(result).isSameAs(dtoResp);
    }

    @Test
    void whenSessionNotFound_onGetBySession_thenThrowNotFoundException() {
        when(sessionRepo.findById(202L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRecordingsBySession(202L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Session not found with id 202");
    }

    @Test
    void whenRecordingsExist_onGetBySession_thenReturnDtoList() {
        when(sessionRepo.findById(101L)).thenReturn(Optional.of(session));

        var rec1 = SessionRecording.builder().id(1L).build();
        var rec2 = SessionRecording.builder().id(2L).build();
        when(recordingRepo.findBySessionId(101L)).thenReturn(List.of(rec1, rec2));

        var d1 = new SessionRecordingResponseDto(); d1.setId(1L);
        var d2 = new SessionRecordingResponseDto(); d2.setId(2L);
        when(mapper.toDto(rec1)).thenReturn(d1);
        when(mapper.toDto(rec2)).thenReturn(d2);

        var out = service.getRecordingsBySession(101L);
        assertThat(out).containsExactly(d1, d2);
    }

    @Test
    void whenGetForCurrentUser_thenReturnCombinedList() {
        authenticateAs(mentor);
        var recMentor = SessionRecording.builder().id(3L).session(session).build();
        var recMentee = SessionRecording.builder().id(4L).session(session).build();

        when(recordingRepo.findAllBySession_MentorId(mentor.getId()))
                .thenReturn(List.of(recMentor));
        when(recordingRepo.findAllBySession_MenteeId(mentor.getId()))
                .thenReturn(List.of(recMentee));

        var dM = new SessionRecordingResponseDto(); dM.setId(3L);
        var dE = new SessionRecordingResponseDto(); dE.setId(4L);
        when(mapper.toDto(recMentor)).thenReturn(dM);
        when(mapper.toDto(recMentee)).thenReturn(dE);

        var out = service.getRecordingsForCurrentUser();
        assertThat(out).containsExactlyInAnyOrder(dM, dE);
    }

    @Test
    void whenDeleteRecordingNotFound_thenThrowNotFoundException() {
        when(recordingRepo.findById(777L)).thenReturn(Optional.empty());
        authenticateAs(mentor);

        assertThatThrownBy(() -> service.deleteRecording(777L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Recording not found with id 777");
    }

    @Test
    void whenNonMentor_onDelete_thenThrowAccessDeniedException() {
        var rec = SessionRecording.builder()
                .id(88L)
                .session(session)
                .build();
        when(recordingRepo.findById(88L)).thenReturn(Optional.of(rec));
        authenticateAs(mentee);

        assertThatThrownBy(() -> service.deleteRecording(88L))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessage("Only the mentor can delete recordings");
    }

    @Test
    void whenMentor_onDelete_thenDeleteRecording() {
        var rec = SessionRecording.builder()
                .id(99L)
                .session(session)
                .build();
        when(recordingRepo.findById(99L)).thenReturn(Optional.of(rec));
        authenticateAs(mentor);

        service.deleteRecording(99L);
        verify(recordingRepo).delete(rec);
    }
}
