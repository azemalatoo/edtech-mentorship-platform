package alatoo.edu.edtechmentorshipplatform.dto.mentee;

import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenteeProfileResponseDto {
    private UUID id;
    private String educationLevel;
    private String fieldOfStudy;
    private String careerGoal;
    private Lang preferredLanguage;
}
