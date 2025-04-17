package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.LearningGoal;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.LearningGoalMapper;
import alatoo.edu.edtechmentorshipplatform.repo.LearningGoalRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.LearningGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LearningGoalServiceImpl implements LearningGoalService {

    private final LearningGoalRepo learningGoalRepository;
    private final UserRepo userRepository;

    @Override
    public LearningGoalResponseDto createGoal(LearningGoalRequestDto requestDto) {
        User mentee = userRepository.findById(requestDto.getMenteeId())
                .orElseThrow(() -> new NotFoundException("Mentee not found"));
        User mentor = requestDto.getMentorId() != null ?
                userRepository.findById(requestDto.getMentorId()).orElse(null) : null;

        LearningGoal goal = LearningGoalMapper.toEntity(requestDto, mentee, mentor);
        return LearningGoalMapper.toDto(learningGoalRepository.save(goal));
    }

    @Override
    public LearningGoalResponseDto updateGoal(UUID goalId, LearningGoalRequestDto requestDto) {
        LearningGoal goal = learningGoalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        goal.setGoalTitle(requestDto.getGoalTitle());
        goal.setDescription(requestDto.getDescription());
        goal.setStatus(requestDto.getStatus());

        if (requestDto.getMentorId() != null) {
            User mentor = userRepository.findById(requestDto.getMentorId())
                    .orElseThrow(() -> new NotFoundException("Mentor not found"));
            goal.setMentor(mentor);
        }

        return LearningGoalMapper.toDto(learningGoalRepository.save(goal));
    }

    @Override
    public LearningGoalResponseDto markAsAchieved(UUID goalId, String feedback) {
        LearningGoal goal = learningGoalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        goal.setAchievedAt(LocalDateTime.now());
        goal.setIsAchieved(Boolean.TRUE);
        goal.setStatus(GoalStatus.ACHIEVED);
        goal.setFeedback(feedback);

        return LearningGoalMapper.toDto(learningGoalRepository.save(goal));
    }

    @Override
    public LearningGoalResponseDto getGoalById(UUID goalId) {
        LearningGoal goal = learningGoalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        return LearningGoalMapper.toDto(goal);
    }

    @Override
    public List<LearningGoalResponseDto> getGoalsByMentee(UUID menteeId) {
        return learningGoalRepository.findByMenteeId(menteeId)
                .stream()
                .map(LearningGoalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGoal(UUID goalId) {
        learningGoalRepository.deleteById(goalId);
    }

    @Override
    public List<LearningGoalResponseDto> getAllGoals() {
        return learningGoalRepository.findAll()
                .stream()
                .map(LearningGoalMapper::toDto)
                .collect(Collectors.toList());
    }
}
