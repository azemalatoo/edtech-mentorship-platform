package alatoo.edu.edtechmentorshipplatform.dto.users;

import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MentorProfileRequestDto {

    @NotNull(message = "User ID must not be null")
    private UUID userID;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    @NotBlank(message = "Headline must not be blank")
    @Size(max = 255, message = "Headline must not exceed 255 characters")
    private String headline;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private int yearsExperience;

    @Pattern(
            regexp = "^(https?://)?(www\\.)?linkedin\\.com/.*$",
            message = "LinkedIn URL must be a valid LinkedIn profile"
    )
    private String linkedinUrl;

    private List<@NotBlank(message = "Certification name cannot be blank") String> certifications;

    private List<Lang> languages;

    @NotNull(message = "Expertise category ID is required")
    private UUID expertiseCategoryId;

    @Min(value = 0, message = "Total mentees must be 0 or more")
    private int totalMentees;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5.0")
    private double averageRating;
}
