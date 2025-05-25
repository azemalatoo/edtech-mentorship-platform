package alatoo.edu.edtechmentorshipplatform.dto.mentee;

import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MenteeProfileRequestDto {

    @NotNull(message = "User ID must not be null")
    private UUID userID;

    @NotBlank(message = "Education level is required")
    private String educationLevel;

    @NotBlank(message = "Field of study is required")
    private String fieldOfStudy;

    @NotBlank(message = "Career goal is required")
    private String careerGoal;

    @NotBlank(message = "Preferred language is required")
    private Lang preferredLanguage;
}
