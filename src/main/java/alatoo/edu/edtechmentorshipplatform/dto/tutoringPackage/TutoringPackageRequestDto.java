package alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage;

import alatoo.edu.edtechmentorshipplatform.enums.TutoringPackageStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TutoringPackageRequestDto {
    @NotNull
    private UUID mentorId;

    @NotBlank
    private String title;

    private String description;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    @Min(1)
    private int durationDays;

    @Min(1)
    private int sessionLimit;

    private boolean supportIncluded;

    @NotNull
    private TutoringPackageStatus status;
}
