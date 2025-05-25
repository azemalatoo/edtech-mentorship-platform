package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.LearningGoal;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import org.springframework.stereotype.Component;

@Component
public class LearningGoalMapper {
    public LearningGoal toEntity(LearningGoalRequestDto dto, User mentee, User mentor) {
        return LearningGoal.builder()
                .mentee(mentee)
                .mentor(mentor)
                .goalTitle(dto.getGoalTitle())
                .description(dto.getDescription())
                .targetDate(dto.getTargetDate())
                .progressPercentage(dto.getProgressPercentage() != null ? dto.getProgressPercentage() : 0)
                .progressNotes(dto.getProgressNotes())
                .status(dto.getStatus() != null ? dto.getStatus() : GoalStatus.IN_PROGRESS)
                .feedback(dto.getFeedback())
                .build();
    }

    public LearningGoalResponseDto toDto(LearningGoal entity) {
        return LearningGoalResponseDto.builder()
                .id(entity.getId())
                .menteeId(entity.getMentee().getId())
                .mentorId(entity.getMentor() != null ? entity.getMentor().getId() : null)
                .goalTitle(entity.getGoalTitle())
                .description(entity.getDescription())
                .isAchieved(entity.getIsAchieved())
                .createdAt(entity.getCreatedAt())
                .achievedAt(entity.getAchievedAt())
                .targetDate(entity.getTargetDate())
                .progressPercentage(entity.getProgressPercentage())
                .progressNotes(entity.getProgressNotes())
                .status(entity.getStatus())
                .feedback(entity.getFeedback())
                .build();
    }
}