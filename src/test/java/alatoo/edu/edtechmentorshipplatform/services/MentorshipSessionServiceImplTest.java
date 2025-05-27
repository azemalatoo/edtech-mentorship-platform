package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.session.MentorshipSessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionSlotRequestDto;
import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.SessionProviderType;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import alatoo.edu.edtechmentorshipplatform.exception.EntityInUseException;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.MentorshipSessionMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MenteeProfileRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MentorshipSessionRepo;
import alatoo.edu.edtechmentorshipplatform.security.UserDetailsImpl;
import alatoo.edu.edtechmentorshipplatform.services.impl.MentorshipSessionServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipSessionServiceImplTest {

    @Mock
    private MentorshipSessionRepo repo;

    @Mock
    private MentorshipSessionMapper mapper;

    @Mock
    private MenteeProfileRepo menteeProfileRepo;

    @InjectMocks
    private MentorshipSessionServiceImpl service;

    private User mentor;
    private User mentee;

    @BeforeEach
    void setUp() {
        mentor = User.builder().id(UUID.randomUUID()).build();
        mentee = User.builder().id(UUID.randomUUID()).build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(User u) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(new UserDetailsImpl(u), null, null)
        );
    }

    @Test
    void createSlot_shouldSaveAndReturnDto() {
        authenticateAs(mentor);
        SessionSlotRequestDto req = new SessionSlotRequestDto();
        req.setAvailableFrom(LocalDateTime.now().plusDays(1));
        req.setAvailableTo(req.getAvailableFrom().plusHours(1));
        req.setProviderType(SessionProviderType.ZOOM);
        req.setMeetingLink("https://zoom/meet");
        req.setNotes("note");

        MentorshipSession toSave = MentorshipSession.builder().build();
        MentorshipSession saved = MentorshipSession.builder().id(99L).mentor(mentor).build();
        MentorshipSessionResponseDto dto = MentorshipSessionResponseDto.builder().id(99L).build();

        when(mapper.toSlotEntity(req, mentor)).thenReturn(toSave);
        when(repo.save(toSave)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        // act
        MentorshipSessionResponseDto result = service.createSlot(req);

        // assert
        assertThat(result).isSameAs(dto);
        verify(mapper).toSlotEntity(req, mentor);
        verify(repo).save(toSave);
        verify(mapper).toDto(saved);
    }

    @Test
    void updateSlot_ownerAndAvailable_updates() {
        authenticateAs(mentor);
        Long slotId = 1L;
        SessionSlotRequestDto req = new SessionSlotRequestDto();
        req.setAvailableFrom(LocalDateTime.now().plusDays(2));
        req.setAvailableTo(req.getAvailableFrom().plusHours(2));
        req.setProviderType(SessionProviderType.GOOGLE_MEET);
        req.setMeetingLink("link2");
        req.setNotes("new notes");

        MentorshipSession slot = MentorshipSession.builder()
                .id(slotId)
                .mentor(mentor)
                .status(SessionStatus.AVAILABLE)
                .build();
        MentorshipSession saved = MentorshipSession.builder().id(slotId).build();
        MentorshipSessionResponseDto dto = MentorshipSessionResponseDto.builder().id(slotId).build();

        when(repo.findById(slotId)).thenReturn(Optional.of(slot));
        when(repo.save(slot)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        // act
        MentorshipSessionResponseDto result = service.updateSlot(slotId, req);

        // assert
        assertThat(result).isSameAs(dto);
        assertThat(slot.getAvailableFrom()).isEqualTo(req.getAvailableFrom());
        assertThat(slot.getNotes()).isEqualTo("new notes");
        verify(repo).findById(slotId);
        verify(repo).save(slot);
        verify(mapper).toDto(saved);
    }

    @Test
    void updateSlot_notOwner_throws() {
        authenticateAs(mentee);
        Long slotId = 2L;
        MentorshipSession slot = MentorshipSession.builder()
                .id(slotId)
                .mentor(mentor)
                .status(SessionStatus.AVAILABLE)
                .build();
        when(repo.findById(slotId)).thenReturn(Optional.of(slot));

        assertThatThrownBy(() -> service.updateSlot(slotId, new SessionSlotRequestDto()))
            .isInstanceOf(EntityInUseException.class)
            .hasMessage("Not the owner of this slot");
    }

    @Test
    void updateSlot_notAvailable_throws() {
        authenticateAs(mentor);
        Long slotId = 3L;
        MentorshipSession slot = MentorshipSession.builder()
                .id(slotId)
                .mentor(mentor)
                .status(SessionStatus.BOOKED)
                .build();
        when(repo.findById(slotId)).thenReturn(Optional.of(slot));

        assertThatThrownBy(() -> service.updateSlot(slotId, new SessionSlotRequestDto()))
            .isInstanceOf(EntityInUseException.class)
            .hasMessage("Only available slots can be updated");
    }

    @Test
    void getSlotsByMentorAndStatus_mapsAll() {
        UUID mid = mentor.getId();
        MentorshipSession s1 = MentorshipSession.builder().id(10L).build();
        MentorshipSession s2 = MentorshipSession.builder().id(11L).build();
        MentorshipSessionResponseDto d1 = MentorshipSessionResponseDto.builder().id(10L).build();
        MentorshipSessionResponseDto d2 = MentorshipSessionResponseDto.builder().id(11L).build();
        when(repo.findByMentorIdAndStatus(mid, SessionStatus.AVAILABLE))
            .thenReturn(List.of(s1, s2));
        when(mapper.toDto(s1)).thenReturn(d1);
        when(mapper.toDto(s2)).thenReturn(d2);

        List<MentorshipSessionResponseDto> out = service.getSlotsByMentorAndStatus(mid, SessionStatus.AVAILABLE);

        assertThat(out).containsExactly(d1, d2);
        verify(repo).findByMentorIdAndStatus(mid, SessionStatus.AVAILABLE);
    }

    @Test
    void bookSlot_available_assignsMenteeAndStatus() {
        // current user is mentee
        authenticateAs(mentee);

        Long slotId = 4L;
        MentorshipSession slot = MentorshipSession.builder()
                .id(slotId)
                .status(SessionStatus.AVAILABLE)
                .build();
        MentorshipSession saved = MentorshipSession.builder().id(slotId).build();
        MentorshipSessionResponseDto dto = MentorshipSessionResponseDto.builder().id(slotId).build();

        when(repo.findById(slotId)).thenReturn(Optional.of(slot));
        when(repo.save(slot)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        MentorshipSessionResponseDto out = service.bookSlot(slotId);

        assertThat(out).isSameAs(dto);
        assertThat(slot.getMentee()).extracting(User::getId).isEqualTo(mentee.getId());
        assertThat(slot.getStatus()).isEqualTo(SessionStatus.BOOKED);
    }

    @Test
    void bookSlot_notAvailable_throws() {
        authenticateAs(mentee);
        Long slotId = 5L;
        MentorshipSession slot = MentorshipSession.builder()
                .id(slotId)
                .status(SessionStatus.BOOKED)
                .build();
        when(repo.findById(slotId)).thenReturn(Optional.of(slot));

        assertThatThrownBy(() -> service.bookSlot(slotId))
            .isInstanceOf(EntityInUseException.class)
            .hasMessage("Slot not available");
    }

    @Test
    void startSession_successAndMapping() {
        authenticateAs(mentor);
        Long id = 6L;
        MentorshipSession session = MentorshipSession.builder()
                .id(id)
                .mentor(mentor)
                .status(SessionStatus.BOOKED)
                .build();
        MentorshipSession saved = MentorshipSession.builder().id(id).build();
        MentorshipSessionResponseDto dto = MentorshipSessionResponseDto.builder().id(id).build();

        when(repo.findById(id)).thenReturn(Optional.of(session));
        when(repo.save(session)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        MentorshipSessionResponseDto out = service.startSession(id);

        assertThat(out).isSameAs(dto);
        assertThat(session.getStatus()).isEqualTo(SessionStatus.IN_PROGRESS);
        assertThat(session.getStartedAt()).isNotNull();
    }

    @Test
    void startSession_wrongMentor_throws() {
        authenticateAs(mentee);
        Long id = 7L;
        MentorshipSession session = MentorshipSession.builder()
                .id(id)
                .mentor(mentor)
                .status(SessionStatus.BOOKED)
                .build();
        when(repo.findById(id)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> service.startSession(id))
            .isInstanceOf(EntityInUseException.class)
            .hasMessage("Not mentor of this session");
    }

    @Test
    void completeSession_success() {
        authenticateAs(mentor);
        Long id = 8L;
        MentorshipSession session = MentorshipSession.builder()
                .id(id)
                .mentor(mentor)
                .status(SessionStatus.IN_PROGRESS)
                .build();
        MentorshipSession saved = MentorshipSession.builder().id(id).build();
        MentorshipSessionResponseDto dto = MentorshipSessionResponseDto.builder().id(id).build();

        when(repo.findById(id)).thenReturn(Optional.of(session));
        when(repo.save(session)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        MentorshipSessionResponseDto out = service.completeSession(id);

        assertThat(out).isSameAs(dto);
        assertThat(session.getStatus()).isEqualTo(SessionStatus.COMPLETED);
        assertThat(session.getEndedAt()).isNotNull();
    }

    @Test
    void completeSession_notInProgress_throws() {
        authenticateAs(mentor);
        Long id = 9L;
        MentorshipSession session = MentorshipSession.builder()
                .id(id)
                .mentor(mentor)
                .status(SessionStatus.AVAILABLE)
                .build();
        when(repo.findById(id)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> service.completeSession(id))
            .isInstanceOf(EntityInUseException.class)
            .hasMessage("Session not in progress");
    }

    @Test
    void cancelSession_byMentor_success() {
        authenticateAs(mentor);
        Long id = 10L;
        MentorshipSession session = MentorshipSession.builder()
                .id(id)
                .mentor(mentor)
                .status(SessionStatus.AVAILABLE)
                .build();
        MentorshipSession saved = MentorshipSession.builder().id(id).build();
        MentorshipSessionResponseDto dto = MentorshipSessionResponseDto.builder().id(id).build();

        when(repo.findById(id)).thenReturn(Optional.of(session));
        when(repo.save(session)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        MentorshipSessionResponseDto out = service.cancelSession(id);

        assertThat(out).isSameAs(dto);
        assertThat(session.getStatus()).isEqualTo(SessionStatus.CANCELLED);
    }

    @Test
    void cancelSession_afterStart_throws() {
        authenticateAs(mentor);
        Long id = 11L;
        MentorshipSession session = MentorshipSession.builder()
                .id(id)
                .mentor(mentor)
                .status(SessionStatus.IN_PROGRESS)
                .build();
        when(repo.findById(id)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> service.cancelSession(id))
            .isInstanceOf(EntityInUseException.class)
            .hasMessage("Cannot cancel after start");
    }

    @Test
    void cancelSession_byMentee_success() {
        authenticateAs(mentee);
        Long id = 12L;
        MentorshipSession session = MentorshipSession.builder()
                .id(id)
                .mentor(mentor)
                .mentee(mentee)
                .status(SessionStatus.BOOKED)
                .build();
        MentorshipSession saved = MentorshipSession.builder().id(id).build();
        MentorshipSessionResponseDto dto = MentorshipSessionResponseDto.builder().id(id).build();

        when(repo.findById(id)).thenReturn(Optional.of(session));
        when(repo.save(session)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        MentorshipSessionResponseDto out = service.cancelSession(id);

        assertThat(out).isSameAs(dto);
        assertThat(session.getStatus()).isEqualTo(SessionStatus.CANCELLED);
    }

    @Test
    void getSessionsByMentee_withStatus_filters() {
        authenticateAs(mentee);

        // simulate MenteeProfile lookup
        MenteeProfile mp = MenteeProfile.builder().id(UUID.randomUUID()).build();
        when(menteeProfileRepo.findByUserId(mentee.getId())).thenReturn(Optional.of(mp));

        MentorshipSession s1 = MentorshipSession.builder().id(20L).build();
        MentorshipSessionResponseDto d1 = MentorshipSessionResponseDto.builder().id(20L).build();
        when(repo.findByMenteeIdAndStatus(mp.getId(), SessionStatus.COMPLETED))
            .thenReturn(List.of(s1));
        when(mapper.toDto(s1)).thenReturn(d1);

        List<MentorshipSessionResponseDto> out =
            service.getSessionsByMentee(SessionStatus.COMPLETED);

        assertThat(out).containsExactly(d1);
    }

    @Test
    void getSessionsByMentee_noStatus_returnsAll() {
        authenticateAs(mentee);

        MenteeProfile mp = MenteeProfile.builder().id(UUID.randomUUID()).build();
        when(menteeProfileRepo.findByUserId(mentee.getId())).thenReturn(Optional.of(mp));

        MentorshipSession s2 = MentorshipSession.builder().id(21L).build();
        MentorshipSessionResponseDto d2 = MentorshipSessionResponseDto.builder().id(21L).build();
        when(repo.findByMenteeId(mp.getId())).thenReturn(List.of(s2));
        when(mapper.toDto(s2)).thenReturn(d2);

        List<MentorshipSessionResponseDto> out = service.getSessionsByMentee(null);

        assertThat(out).containsExactly(d2);
    }

    @Test
    void getSessionsByMentee_noProfile_throws() {
        authenticateAs(mentee);
        when(menteeProfileRepo.findByUserId(mentee.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSessionsByMentee(null))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Mentee profile with id");
    }
}
