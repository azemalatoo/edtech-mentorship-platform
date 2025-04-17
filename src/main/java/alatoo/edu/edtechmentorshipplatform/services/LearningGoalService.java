package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;

import java.util.List;
import java.util.UUID;

public interface LearningGoalService {
    LearningGoalResponseDto createGoal(LearningGoalRequestDto requestDto);
    LearningGoalResponseDto updateGoal(UUID goalId, LearningGoalRequestDto requestDto);
    LearningGoalResponseDto markAsAchieved(UUID goalId, String feedback);
    LearningGoalResponseDto getGoalById(UUID goalId);
    List<LearningGoalResponseDto> getGoalsByMentee(UUID menteeId);
    void deleteGoal(UUID goalId);
    List<LearningGoalResponseDto> getAllGoals();
}
