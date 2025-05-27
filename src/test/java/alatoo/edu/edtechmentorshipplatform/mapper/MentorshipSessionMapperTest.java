package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.session.MentorshipSessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionSlotRequestDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.SessionProviderType;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MentorshipSessionMapperTest {

    private final MentorshipSessionMapper mapper = new MentorshipSessionMapper();

    @Test
    void toSlotEntity_allFieldsProvided_shouldMapCorrectly() {
        // given
        LocalDateTime from = LocalDateTime.of(2025, 5, 28, 10, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 5, 28, 11, 0);
        SessionSlotRequestDto dto = new SessionSlotRequestDto();
        dto.setAvailableFrom(from);
        dto.setAvailableTo(to);
        dto.setProviderType(SessionProviderType.ZOOM);
        dto.setMeetingLink("https://zoom.us/j/123456789");
        dto.setNotes("Bring slides");

        User mentor = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000A")).build();

        // when
        MentorshipSession session = mapper.toSlotEntity(dto, mentor);

        // then
        assertThat(session).isNotNull();
        assertThat(session.getMentor()).isSameAs(mentor);
        assertThat(session.getAvailableFrom()).isEqualTo(from);
        assertThat(session.getAvailableTo()).isEqualTo(to);
        assertThat(session.getProviderType()).isEqualTo(SessionProviderType.ZOOM);
        assertThat(session.getMeetingLink()).isEqualTo("https://zoom.us/j/123456789");
        assertThat(session.getNotes()).isEqualTo("Bring slides");
        assertThat(session.getStatus()).isEqualTo(SessionStatus.AVAILABLE);
    }

    @Test
    void toSlotEntity_nullDto_shouldThrowNpe() {
        User mentor = User.builder().id(UUID.randomUUID()).build();
        assertThrows(NullPointerException.class,
            () -> mapper.toSlotEntity(null, mentor));
    }

    @Test
    void toDto_fullEntity_shouldMapAllFields() {
        // given
        UUID sessionId = UUID.randomUUID();
        User mentor = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000B")).build();
        User mentee = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000C")).build();
        LocalDateTime from       = LocalDateTime.of(2025, 6, 1, 14, 0);
        LocalDateTime to         = LocalDateTime.of(2025, 6, 1, 15, 0);
        LocalDateTime startedAt  = from.plusMinutes(5);
        LocalDateTime endedAt    = to.minusMinutes(5);

        MentorshipSession entity = MentorshipSession.builder()
            .id(123L)
            .mentor(mentor)
            .mentee(mentee)
            .availableFrom(from)
            .availableTo(to)
            .startedAt(startedAt)
            .endedAt(endedAt)
            .status(SessionStatus.COMPLETED)
            .providerType(SessionProviderType.GOOGLE_MEET)
            .meetingLink("meet.google.com/abc-defg-hij")
            .notes("Great session")
            .build();

        // when
        MentorshipSessionResponseDto dto = mapper.toDto(entity);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(123L);
        assertThat(dto.getMentorId()).isEqualTo(mentor.getId());
        assertThat(dto.getMenteeId()).isEqualTo(mentee.getId());
        assertThat(dto.getAvailableFrom()).isEqualTo(from);
        assertThat(dto.getAvailableTo()).isEqualTo(to);
        assertThat(dto.getStartedAt()).isEqualTo(startedAt);
        assertThat(dto.getEndedAt()).isEqualTo(endedAt);
        assertThat(dto.getStatus()).isEqualTo(SessionStatus.COMPLETED);
        assertThat(dto.getProviderType()).isEqualTo(SessionProviderType.GOOGLE_MEET);
        assertThat(dto.getMeetingLink()).isEqualTo("meet.google.com/abc-defg-hij");
        assertThat(dto.getNotes()).isEqualTo("Great session");
    }

    @Test
    void toDto_withNullMentee_shouldSetMenteeIdNull() {
        // given
        User mentor = User.builder().id(UUID.fromString("00000000-0000-0000-0000-00000000000D")).build();
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to   = from.plusHours(1);
        MentorshipSession entity = MentorshipSession.builder()
            .id(456L)
            .mentor(mentor)
            .mentee(null)
            .availableFrom(from)
            .availableTo(to)
            .status(SessionStatus.BOOKED)
            .providerType(SessionProviderType.TELEGRAM)
            .meetingLink("https://t.me/joinchat/Example")
            .notes(null)
            .build();

        // when
        MentorshipSessionResponseDto dto = mapper.toDto(entity);

        // then
        assertThat(dto.getMenteeId()).isNull();
        assertThat(dto.getMentorId()).isEqualTo(mentor.getId());
        assertThat(dto.getStatus()).isEqualTo(SessionStatus.BOOKED);
    }

    @Test
    void toDto_nullEntity_shouldThrowNpe() {
        assertThrows(NullPointerException.class,
            () -> mapper.toDto(null));
    }
}
