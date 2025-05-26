package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;

public class MenteeProfileMapper {

    public static MenteeProfile toEntity(MenteeProfileResponseDto dto, User user) {
        return MenteeProfile.builder()
                .id(dto.getId())
                .user(user)
                .educationLevel(dto.getEducationLevel())
                .fieldOfStudy(dto.getFieldOfStudy())
                .careerGoal(dto.getCareerGoal())
                .preferredLanguage(dto.getPreferredLanguage())
                .build();
    }

    public static MenteeProfileResponseDto toDto(MenteeProfile entity) {
        return MenteeProfileResponseDto.builder()
                .id(entity.getId())
                .educationLevel(entity.getEducationLevel())
                .fieldOfStudy(entity.getFieldOfStudy())
                .careerGoal(entity.getCareerGoal())
                .preferredLanguage(entity.getPreferredLanguage())
                .build();
    }
}
