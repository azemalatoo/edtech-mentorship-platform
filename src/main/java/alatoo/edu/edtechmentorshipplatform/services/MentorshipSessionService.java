package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionResponseDto;

import java.util.List;
import java.util.UUID;

public interface MentorshipSessionService {

    SessionResponseDto createSession(SessionRequestDto createSessionRequestDto);

    SessionResponseDto getSessionById(UUID sessionId);

    List<SessionResponseDto> getSessionsByMentor(UUID mentorId);

    List<SessionResponseDto> getSessionsByMentee(UUID menteeId);

    SessionResponseDto updateSessionStatus(UUID sessionId, String status);

    void deleteSession(UUID sessionId);
}
