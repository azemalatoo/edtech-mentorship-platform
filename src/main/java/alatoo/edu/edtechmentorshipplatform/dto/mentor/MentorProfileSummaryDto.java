package alatoo.edu.edtechmentorshipplatform.dto.mentor;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MentorProfileSummaryDto {
    private UUID id;
    private String fullName;
    private String headline;
    private int yearsExperience;
    private double averageRating;
}
