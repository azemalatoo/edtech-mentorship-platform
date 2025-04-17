package alatoo.edu.edtechmentorshipplatform.dto.users;

import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MentorProfileResponseDto {

    private UUID id;
    private String bio;
    private String headline;
    private int yearsExperience;
    private String linkedinUrl;
    private List<String> certifications;
    private List<Lang> languages;
    private CategoryDto expertiseCategory;
    private int totalMentees;
    private double averageRating;
}
