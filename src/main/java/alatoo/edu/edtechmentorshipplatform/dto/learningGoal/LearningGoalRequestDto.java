package alatoo.edu.edtechmentorshipplatform.dto.learningGoal;

import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LearningGoalRequestDto {
    @NotNull
    private UUID menteeId;

    private UUID mentorId;

    @NotBlank
    private String goalTitle;

    private String description;
    private LocalDate targetDate;
    private Integer progressPercentage;
    private String progressNotes;
    private GoalStatus status;
    private String feedback;
}