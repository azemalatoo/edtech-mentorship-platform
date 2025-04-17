package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.LearningGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Learning Goals", description = "Manage learning goals and progress tracking")
public class LearningGoalController {

    private final LearningGoalService learningGoalService;

    @PostMapping
    @Operation(summary = "Create a new learning goal")
    public LearningGoalResponseDto createGoal(@RequestBody @Valid LearningGoalRequestDto dto) {
        return learningGoalService.createGoal(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing learning goal")
    public LearningGoalResponseDto updateGoal(@PathVariable UUID id,
                                              @RequestBody @Valid LearningGoalRequestDto dto) {
        return learningGoalService.updateGoal(id, dto);
    }

    @PutMapping("/{id}/achieve")
    @Operation(summary = "Mark goal as achieved with feedback")
    public LearningGoalResponseDto markAsAchieved(@PathVariable UUID id,
                                                  @RequestParam String feedback) {
        return learningGoalService.markAsAchieved(id, feedback);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get goal by ID")
    public LearningGoalResponseDto getById(@PathVariable UUID id) {
        return learningGoalService.getGoalById(id);
    }

    @GetMapping("/mentee/{menteeId}")
    @Operation(summary = "Get goals by mentee ID")
    public List<LearningGoalResponseDto> getByMentee(@PathVariable UUID menteeId) {
        return learningGoalService.getGoalsByMentee(menteeId);
    }

    @GetMapping
    @Operation(summary = "Get all learning goals")
    public List<LearningGoalResponseDto> getAll() {
        return learningGoalService.getAllGoals();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a goal")
    public void deleteGoal(@PathVariable UUID id) {
        learningGoalService.deleteGoal(id);
    }
}
