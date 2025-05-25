package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.session.BookingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.MentorshipSessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionSlotRequestDto;
import java.util.List;

public interface MentorshipSessionService {
    MentorshipSessionResponseDto createSlot(SessionSlotRequestDto dto);
    MentorshipSessionResponseDto updateSlot(Long slotId, SessionSlotRequestDto dto);
    List<MentorshipSessionResponseDto> getSlotsByMentorAndAvailability(java.util.UUID mentorId);
    MentorshipSessionResponseDto bookSlot(Long slotId, BookingRequestDto dto);
    MentorshipSessionResponseDto startSession(Long sessionId);
    MentorshipSessionResponseDto completeSession(Long sessionId);
    MentorshipSessionResponseDto cancelSession(Long sessionId);
    List<MentorshipSessionResponseDto> getSessionsByMentee(java.util.UUID menteeId, alatoo.edu.edtechmentorshipplatform.enums.SessionStatus status);
}
