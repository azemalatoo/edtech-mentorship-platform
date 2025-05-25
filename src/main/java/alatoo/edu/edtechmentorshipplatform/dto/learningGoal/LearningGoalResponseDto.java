package alatoo.edu.edtechmentorshipplatform.dto.learningGoal;

import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LearningGoalResponseDto {
    private Long id;
    private UUID menteeId;
    private UUID mentorId;
    private String goalTitle;
    private String description;
    private Boolean isAchieved;
    private LocalDateTime createdAt;
    private LocalDateTime achievedAt;
    private LocalDate targetDate;
    private Integer progressPercentage;
    private String progressNotes;
    private GoalStatus status;
    private String feedback;
}