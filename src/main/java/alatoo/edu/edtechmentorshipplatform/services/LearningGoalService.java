package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;

import java.util.List;
import java.util.UUID;

public interface LearningGoalService {
    LearningGoalResponseDto createGoal(LearningGoalRequestDto dto);
    LearningGoalResponseDto updateGoal(Long id, LearningGoalRequestDto dto);
    LearningGoalResponseDto markAsAchieved(Long id, String feedback);
    LearningGoalResponseDto updateProgress(Long id, Integer percent, String notes);
    LearningGoalResponseDto changeStatus(Long id, GoalStatus status);
    LearningGoalResponseDto getGoalById(Long id);
    List<LearningGoalResponseDto> getGoalsByMentee(UUID menteeId);
    void deleteGoal(Long id);
}
