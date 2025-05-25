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
    private final LearningGoalRepo goalRepo;
    private final UserRepo userRepo;
    private final LearningGoalMapper mapper;

    @Override
    public LearningGoalResponseDto createGoal(LearningGoalRequestDto dto) {
        User mentee = userRepo.findById(dto.getMenteeId())
                .orElseThrow(() -> new NotFoundException("Mentee not found"));
        User mentor = dto.getMentorId() != null
                ? userRepo.findById(dto.getMentorId()).orElse(null)
                : null;
        LearningGoal entity = mapper.toEntity(dto, mentee, mentor);
        entity.setStatus(GoalStatus.IN_PROGRESS);
        LearningGoal saved = goalRepo.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public LearningGoalResponseDto updateGoal(Long id, LearningGoalRequestDto dto) {
        LearningGoal goal = goalRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        if (dto.getGoalTitle() != null) goal.setGoalTitle(dto.getGoalTitle());
        if (dto.getDescription() != null) goal.setDescription(dto.getDescription());
        if (dto.getTargetDate() != null) goal.setTargetDate(dto.getTargetDate());
        if (dto.getProgressPercentage() != null) goal.setProgressPercentage(dto.getProgressPercentage());
        if (dto.getProgressNotes() != null) goal.setProgressNotes(dto.getProgressNotes());
        if (dto.getStatus() != null) goal.setStatus(dto.getStatus());
        return mapper.toDto(goalRepo.save(goal));
    }

    @Override
    public LearningGoalResponseDto markAsAchieved(Long id, String feedback) {
        LearningGoal goal = goalRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        goal.setIsAchieved(true);
        goal.setStatus(GoalStatus.ACHIEVED);
        goal.setAchievedAt(LocalDateTime.now());
        goal.setFeedback(feedback);
        return mapper.toDto(goalRepo.save(goal));
    }

    @Override
    public LearningGoalResponseDto updateProgress(Long id, Integer percent, String notes) {
        LearningGoal goal = goalRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        goal.setProgressPercentage(percent);
        goal.setProgressNotes(notes);
        if (percent != null && percent >= 100) {
            goal.setIsAchieved(true);
            goal.setStatus(GoalStatus.ACHIEVED);
            goal.setAchievedAt(LocalDateTime.now());
        }
        return mapper.toDto(goalRepo.save(goal));
    }

    @Override
    public LearningGoalResponseDto changeStatus(Long id, GoalStatus status) {
        LearningGoal goal = goalRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
        goal.setStatus(status);
        if (status == GoalStatus.ACHIEVED && !goal.getIsAchieved()) {
            goal.setIsAchieved(true);
            goal.setAchievedAt(LocalDateTime.now());
        }
        return mapper.toDto(goalRepo.save(goal));
    }

    @Override
    public LearningGoalResponseDto getGoalById(Long id) {
        return goalRepo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Goal not found"));
    }

    @Override
    public List<LearningGoalResponseDto> getGoalsByMentee(UUID menteeId) {
        return goalRepo.findByMentee_Id(menteeId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGoal(Long id) {
        if (!goalRepo.existsById(id)) {
            throw new NotFoundException("Goal not found");
        }
        goalRepo.deleteById(id);
    }
}
