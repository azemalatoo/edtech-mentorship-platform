package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.learningGoal.LearningGoalResponseDto;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import alatoo.edu.edtechmentorshipplatform.services.LearningGoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new learning goal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Learning goal created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseApi<LearningGoalResponseDto> createGoal(
            @RequestBody @Valid LearningGoalRequestDto dto) {
        LearningGoalResponseDto result = learningGoalService.createGoal(dto);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing learning goal")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Learning goal updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Learning goal not found")
    })
    public ResponseApi<LearningGoalResponseDto> updateGoal(
            @PathVariable Long id,
            @RequestBody @Valid LearningGoalRequestDto dto) {
        LearningGoalResponseDto result = learningGoalService.updateGoal(id, dto);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @PutMapping("/{id}/achieve")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Mark goal as achieved with feedback")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Learning goal marked as achieved"),
            @ApiResponse(responseCode = "404", description = "Learning goal not found")
    })
    public ResponseApi<LearningGoalResponseDto> markAsAchieved(
            @PathVariable Long id,
            @RequestParam String feedback) {
        LearningGoalResponseDto result = learningGoalService.markAsAchieved(id, feedback);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get goal by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Learning goal retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Learning goal not found")
    })
    public ResponseApi<LearningGoalResponseDto> getById(@PathVariable Long id) {
        LearningGoalResponseDto result = learningGoalService.getGoalById(id);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @GetMapping("/mentee/{menteeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get goals by mentee ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Learning goals for mentee retrieved successfully")
    })
    public ResponseApi<List<LearningGoalResponseDto>> getByMentee(@PathVariable UUID menteeId) {
        List<LearningGoalResponseDto> result = learningGoalService.getGoalsByMentee(menteeId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all learning goals")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All learning goals retrieved successfully")
    })
    public ResponseApi<List<LearningGoalResponseDto>> getAll() {
        List<LearningGoalResponseDto> result = learningGoalService.getAllGoals();
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a goal")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Learning goal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Learning goal not found")
    })
    public ResponseApi<Void> deleteGoal(@PathVariable Long id) {
        learningGoalService.deleteGoal(id);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
