package alatoo.edu.edtechmentorshipplatform.dto.mentor;

import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorProfileResponseDto {
    private UUID id;
    private String fullName;
    private String bio;
    private String headline;
    private int yearsExperience;
    private String linkedinUrl;
    private List<String> certifications;
    private List<Lang> languages;
    private CategoryDto expertiseCategory;
    private Integer totalMentees;
    private Double averageRating;
}
