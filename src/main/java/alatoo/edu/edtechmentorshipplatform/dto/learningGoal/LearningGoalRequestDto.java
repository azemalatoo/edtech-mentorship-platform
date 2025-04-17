package alatoo.edu.edtechmentorshipplatform.dto.learningGoal;

import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LearningGoalRequestDto {
    @NotBlank
    private String goalTitle;

    @NotBlank
    private String description;

    @NotNull
    private UUID menteeId;

    private UUID mentorId;

    @NotNull
    private GoalStatus status;

    private Boolean isAchieved;
}
