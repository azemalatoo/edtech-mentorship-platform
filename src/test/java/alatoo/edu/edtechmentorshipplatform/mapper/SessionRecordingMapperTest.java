package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.SessionRecording;
import alatoo.edu.edtechmentorshipplatform.enums.AccessLevel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SessionRecordingMapperTest {

    private final SessionRecordingMapper mapper = new SessionRecordingMapper();

        @Test
    void toEntity_nullSession_shouldMapSessionNull() {
        // given
        SessionRecordingRequestDto dto = new SessionRecordingRequestDto();
        dto.setSessionId(2L);
        dto.setRecordingUrl("url");
        dto.setAccessLevel(AccessLevel.PRIVATE);

        // when
        SessionRecording entity = mapper.toEntity(dto, null);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getSession()).isNull();
        assertThat(entity.getRecordingUrl()).isEqualTo("url");
        assertThat(entity.getAccessLevel()).isEqualTo(AccessLevel.PRIVATE);
        assertThat(entity.getUploadedAt()).isNotNull();
    }

    @Test
    void toDto_nullEntity_shouldThrowNpe() {
        assertThrows(NullPointerException.class,
            () -> mapper.toDto(null));
    }
}
