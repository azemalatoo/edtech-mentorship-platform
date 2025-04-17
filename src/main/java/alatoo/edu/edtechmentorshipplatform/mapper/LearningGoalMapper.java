package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.LearningGoal;
import alatoo.edu.edtechmentorshipplatform.entity.User;

public class LearningGoalMapper {

    public static LearningGoal toEntity(LearningGoalRequestDto dto, User mentee, User mentor) {
        return LearningGoal.builder()
                .goalTitle(dto.getGoalTitle())
                .description(dto.getDescription())
                .mentee(mentee)
                .mentor(mentor)
                .status(dto.getStatus())
                .isAchieved(dto.getIsAchieved())
                .build();
    }

    public static LearningGoalResponseDto toDto(LearningGoal goal) {
        LearningGoalResponseDto dto = new LearningGoalResponseDto();
        dto.setId(goal.getId());
        dto.setGoalTitle(goal.getGoalTitle());
        dto.setDescription(goal.getDescription());
        dto.setIsAchieved(goal.getIsAchieved());
        dto.setCreatedAt(goal.getCreatedAt());
        dto.setAchievedAt(goal.getAchievedAt());
        dto.setMenteeId(goal.getMentee().getId());
        if (goal.getMentor() != null)
            dto.setMentorId(goal.getMentor().getId());
        dto.setFeedback(goal.getFeedback());
        dto.setStatus(goal.getStatus());
        return dto;
    }
}
