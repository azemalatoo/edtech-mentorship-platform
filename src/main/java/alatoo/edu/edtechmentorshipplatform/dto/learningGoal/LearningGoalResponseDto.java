package alatoo.edu.edtechmentorshipplatform.dto.learningGoal;

import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LearningGoalResponseDto {
    private UUID id;
    private String goalTitle;
    private String description;
    private Boolean isAchieved;
    private LocalDateTime createdAt;
    private LocalDateTime achievedAt;
    private UUID menteeId;
    private UUID mentorId;
    private String feedback;
    private GoalStatus status;
}
