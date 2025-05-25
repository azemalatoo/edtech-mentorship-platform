package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingResponseDto;

import java.util.List;

public interface SessionRecordingService {
    SessionRecordingResponseDto uploadRecording(SessionRecordingRequestDto requestDto);
    List<SessionRecordingResponseDto> getRecordingsBySession(Long sessionId);
    List<SessionRecordingResponseDto> getRecordingsForCurrentUser();
    void deleteRecording(Long recordingId);
}
