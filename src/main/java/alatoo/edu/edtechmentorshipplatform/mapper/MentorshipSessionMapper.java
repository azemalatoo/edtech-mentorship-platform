package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.session.MentorshipSessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionSlotRequestDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import org.springframework.stereotype.Component;

@Component
public class MentorshipSessionMapper {
    public MentorshipSession toSlotEntity(SessionSlotRequestDto dto, User mentor) {
        return MentorshipSession.builder()
                .mentor(mentor)
                .availableFrom(dto.getAvailableFrom())
                .availableTo(dto.getAvailableTo())
                .meetingLink(dto.getMeetingLink())
                .providerType(dto.getProviderType())
                .status(SessionStatus.AVAILABLE)
                .notes(dto.getNotes())
                .build();
    }

    public MentorshipSessionResponseDto toDto(MentorshipSession e) {
        return MentorshipSessionResponseDto.builder()
                .id(e.getId())
                .mentorId(e.getMentor().getId())
                .menteeId(e.getMentee() != null? e.getMentee().getId():null)
                .availableFrom(e.getAvailableFrom())
                .availableTo(e.getAvailableTo())
                .startedAt(e.getStartedAt())
                .endedAt(e.getEndedAt())
                .status(e.getStatus())
                .providerType(e.getProviderType())
                .meetingLink(e.getMeetingLink())
                .notes(e.getNotes())
                .build();
    }
}
