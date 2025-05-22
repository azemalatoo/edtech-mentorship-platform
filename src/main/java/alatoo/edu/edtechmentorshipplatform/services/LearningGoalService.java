package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;

import java.util.List;
import java.util.UUID;

public interface LearningGoalService {
    LearningGoalResponseDto createGoal(LearningGoalRequestDto requestDto);
    LearningGoalResponseDto updateGoal(Long goalId, LearningGoalRequestDto requestDto);
    LearningGoalResponseDto markAsAchieved(Long goalId, String feedback);
    LearningGoalResponseDto getGoalById(Long goalId);
    List<LearningGoalResponseDto> getGoalsByMentee(UUID menteeId);
    void deleteGoal(Long goalId);
    List<LearningGoalResponseDto> getAllGoals();
}
