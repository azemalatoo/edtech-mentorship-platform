package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.SessionRecording;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionRecordingMapper {

    public SessionRecording toEntity(SessionRecordingRequestDto dto, MentorshipSession session) {
        return SessionRecording.builder()
                .session(session)
                .recordingUrl(dto.getRecordingUrl())
                .uploadedAt(LocalDateTime.now())
                .accessLevel(dto.getAccessLevel())
                .build();
    }

    public SessionRecordingResponseDto toDto(SessionRecording entity) {
        return SessionRecordingResponseDto.builder()
                .id(entity.getId())
                .sessionId(entity.getSession().getId())
                .recordingUrl(entity.getRecordingUrl())
                .uploadedAt(entity.getUploadedAt())
                .accessLevel(entity.getAccessLevel())
                .build();
    }
}