package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import alatoo.edu.edtechmentorshipplatform.services.LearningGoalService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Create a learning goal", description = "Allows a mentee to create a new learning goal.")
    @ApiResponse(responseCode = "201", description = "Goal created successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<LearningGoalResponseDto> create(
            @Valid @RequestBody LearningGoalRequestDto dto) {
        return new ResponseApi<>(service.createGoal(dto), ResponseCode.CREATED);
    }

    @Operation(summary = "Update a learning goal", description = "Allows a mentee to update their existing goal fields.")
    @ApiResponse(responseCode = "200", description = "Goal updated successfully")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<LearningGoalResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody LearningGoalRequestDto dto) {
        return new ResponseApi<>(service.updateGoal(id, dto), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Mark goal as achieved", description = "Marks the specified goal as achieved and optionally records feedback.")
    @ApiResponse(responseCode = "200", description = "Goal marked as achieved")
    @PostMapping("/{id}/achieve")
    @PreAuthorize("hasAnyRole('MENTEE','MENTOR')")
    public ResponseApi<LearningGoalResponseDto> achieve(
            @PathVariable Long id,
            @RequestParam(required = false) String feedback) {
        return new ResponseApi<>(service.markAsAchieved(id, feedback), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Update goal progress", description = "Updates the progress percentage and notes on a goal.")
    @ApiResponse(responseCode = "200", description = "Progress updated")
    @PatchMapping("/{id}/progress")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<LearningGoalResponseDto> progress(
            @PathVariable Long id,
            @RequestParam Integer percent,
            @RequestParam(required = false) String notes) {
        return new ResponseApi<>(service.updateProgress(id, percent, notes), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Change goal status", description = "Allows mentee or mentor to change the goal's status (e.g. on hold, cancelled).")
    @ApiResponse(responseCode = "200", description = "Status changed")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MENTEE','MENTOR')")
    public ResponseApi<LearningGoalResponseDto> changeStatus(
            @PathVariable Long id,
            @RequestParam GoalStatus status) {
        return new ResponseApi<>(service.changeStatus(id, status), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get a goal by ID", description = "Fetches the details of a single learning goal.")
    @ApiResponse(responseCode = "200", description = "Goal retrieved")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MENTEE','MENTOR')")
    public ResponseApi<LearningGoalResponseDto> getById(@PathVariable Long id) {
        return new ResponseApi<>(service.getGoalById(id), ResponseCode.SUCCESS);
    }

    @Operation(summary = "List goals for a mentee", description = "Retrieves all goals created by the specified mentee.")
    @ApiResponse(responseCode = "200", description = "Goals listed")
    @GetMapping("/mentee/{menteeId}")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<List<LearningGoalResponseDto>> listByMentee(@PathVariable UUID menteeId) {
        return new ResponseApi<>(service.getGoalsByMentee(menteeId), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Delete a learning goal", description = "Deletes the specified goal. Only the owning mentee can delete.")
    @ApiResponse(responseCode = "200", description = "Goal deleted")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<Void> delete(@PathVariable Long id) {
        service.deleteGoal(id);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}