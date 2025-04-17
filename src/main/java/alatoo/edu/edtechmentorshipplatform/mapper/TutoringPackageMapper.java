package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.TutoringPackage;
import alatoo.edu.edtechmentorshipplatform.entity.User;

public class TutoringPackageMapper {

    public static TutoringPackage toEntity(TutoringPackageResponseDto dto, User mentor) {
        return TutoringPackage.builder()
                .id(dto.getId())
                .mentor(mentor)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationDays(dto.getDurationDays())
                .sessionLimit(dto.getSessionLimit())
                .supportIncluded(dto.isSupportIncluded())
                .isActive(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    public static TutoringPackageResponseDto toDto(TutoringPackage entity) {
        return TutoringPackageResponseDto.builder()
                .id(entity.getId())
                .mentorId(entity.getMentor().getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .durationDays(entity.getDurationDays())
                .sessionLimit(entity.getSessionLimit())
                .supportIncluded(entity.isSupportIncluded())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
