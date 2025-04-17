package alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage;

import alatoo.edu.edtechmentorshipplatform.enums.TutoringPackageStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TutoringPackageResponseDto {
    private UUID id;
    private UUID mentorId;
    private String title;
    private String description;
    private BigDecimal price;
    private int durationDays;
    private int sessionLimit;
    private boolean supportIncluded;
    private boolean isActive;
    private LocalDateTime createdAt;
    private TutoringPackageStatus status;
}
