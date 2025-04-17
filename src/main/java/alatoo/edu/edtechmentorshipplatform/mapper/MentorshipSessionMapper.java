package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.entity.TutoringPackage;
import alatoo.edu.edtechmentorshipplatform.entity.User;

public class MentorshipSessionMapper {

    public static MentorshipSession toEntity(SessionResponseDto dto, User mentor, User mentee, TutoringPackage tutoringPackage) {
        return MentorshipSession.builder()
                .id(dto.getId())
                .mentor(mentor)
                .mentee(mentee)
                .scheduledAt(dto.getScheduledAt())
                .startedAt(dto.getStartedAt())
                .endedAt(dto.getEndedAt())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .isCompleted(dto.isCompleted())
                .tutoringPackage(tutoringPackage)
                .build();
    }

    public static SessionResponseDto toDto(MentorshipSession entity) {
        return SessionResponseDto.builder()
                .id(entity.getId())
                .mentorId(entity.getMentor().getId())
                .menteeId(entity.getMentee().getId())
                .scheduledAt(entity.getScheduledAt())
                .startedAt(entity.getStartedAt())
                .endedAt(entity.getEndedAt())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .isCompleted(entity.isCompleted())
                .tutoringPackageId(entity.getTutoringPackage() != null ? entity.getTutoringPackage().getId() : null)
                .build();
    }
}
