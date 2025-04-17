package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;

public class MentorProfileMapper {

    public static MentorProfile toEntity(MentorProfileRequestDto dto, User user, Category category) {
        return MentorProfile.builder()
                .user(user)
                .bio(dto.getBio())
                .headline(dto.getHeadline())
                .yearsExperience(dto.getYearsExperience())
                .linkedinUrl(dto.getLinkedinUrl())
                .certifications(dto.getCertifications())
                .languages(dto.getLanguages())
                .expertiseCategory(category)
                .totalMentees(dto.getTotalMentees())
                .averageRating(dto.getAverageRating())
                .build();
    }

    public static MentorProfileResponseDto toDto(MentorProfile mentorProfile) {
        MentorProfileResponseDto dto = new MentorProfileResponseDto();
        dto.setId(mentorProfile.getId());
        dto.setBio(mentorProfile.getBio());
        dto.setHeadline(mentorProfile.getHeadline());
        dto.setYearsExperience(mentorProfile.getYearsExperience());
        dto.setLinkedinUrl(mentorProfile.getLinkedinUrl());
        dto.setCertifications(mentorProfile.getCertifications());
        dto.setLanguages(mentorProfile.getLanguages());
        dto.setExpertiseCategory(CategoryMapper.toDto(mentorProfile.getExpertiseCategory()));
        dto.setTotalMentees(mentorProfile.getTotalMentees());
        dto.setAverageRating(mentorProfile.getAverageRating());
        return dto;
    }
}
