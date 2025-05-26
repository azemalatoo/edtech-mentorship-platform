package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.session.MentorshipSessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionSlotRequestDto;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;

import java.util.List;

public interface MentorshipSessionService {
    MentorshipSessionResponseDto createSlot(SessionSlotRequestDto dto);
    MentorshipSessionResponseDto updateSlot(Long slotId, SessionSlotRequestDto dto);
    List<MentorshipSessionResponseDto> getSlotsByMentorAndStatus(java.util.UUID mentorId, SessionStatus sessionStatus);
    MentorshipSessionResponseDto bookSlot(Long slotId);
    MentorshipSessionResponseDto startSession(Long sessionId);
    MentorshipSessionResponseDto completeSession(Long sessionId);
    MentorshipSessionResponseDto cancelSession(Long sessionId);
    List<MentorshipSessionResponseDto> getSessionsByMentee(SessionStatus status);
}
