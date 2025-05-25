package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import alatoo.edu.edtechmentorshipplatform.services.LearningGoalService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goals")
@Tag(name = "LearningGoalController", description = "APIs for managing learning goals")
@RequiredArgsConstructor
public class LearningGoalController {
    private final LearningGoalService service;

    @PostMapping
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<LearningGoalResponseDto> create(@Valid @RequestBody LearningGoalRequestDto dto) {
        return new ResponseApi<>(service.createGoal(dto), ResponseCode.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<LearningGoalResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody LearningGoalRequestDto dto) {
        return new ResponseApi<>(service.updateGoal(id, dto), ResponseCode.SUCCESS);
    }

    @PostMapping("/{id}/achieve")
    @PreAuthorize("hasAnyRole('MENTEE','MENTOR')")
    public ResponseApi<LearningGoalResponseDto> achieve(
            @PathVariable Long id,
            @RequestParam(required = false) String feedback) {
        return new ResponseApi<>(service.markAsAchieved(id, feedback), ResponseCode.SUCCESS);
    }

    @PatchMapping("/{id}/progress")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<LearningGoalResponseDto> progress(
            @PathVariable Long id,
            @RequestParam Integer percent,
            @RequestParam(required = false) String notes) {
        return new ResponseApi<>(service.updateProgress(id, percent, notes), ResponseCode.SUCCESS);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MENTEE','MENTOR')")
    public ResponseApi<LearningGoalResponseDto> changeStatus(
            @PathVariable Long id,
            @RequestParam GoalStatus status) {
        return new ResponseApi<>(service.changeStatus(id, status), ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MENTEE','MENTOR')")
    public ResponseApi<LearningGoalResponseDto> getById(@PathVariable Long id) {
        return new ResponseApi<>(service.getGoalById(id), ResponseCode.SUCCESS);
    }

    @GetMapping("/mentee/{menteeId}")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<List<LearningGoalResponseDto>> listByMentee(@PathVariable UUID menteeId) {
        return new ResponseApi<>(service.getGoalsByMentee(menteeId), ResponseCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<Void> delete(@PathVariable Long id) {
        service.deleteGoal(id);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}