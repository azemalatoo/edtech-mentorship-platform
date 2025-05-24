package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.controller.base.BaseRestController;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import alatoo.edu.edtechmentorshipplatform.services.MentorshipSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mentorship-sessions")
@RequiredArgsConstructor
@Tag(name = "MentorshipSessionController", description = "APIs for managing mentorship sessions")
public class MentorshipSessionController extends BaseRestController {

    private final MentorshipSessionService mentorshipSessionService;

    @Operation(summary = "Create a new mentorship session", description = "Creates a new session for a mentor and mentee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Session created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<SessionResponseDto> createSession(
            @Valid @RequestBody SessionRequestDto dto) {
        SessionResponseDto result = mentorshipSessionService.createSession(dto);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @Operation(summary = "Get a mentorship session by ID", description = "Fetches the details of a session by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<SessionResponseDto> getSessionById(@PathVariable Long sessionId) {
        SessionResponseDto result = mentorshipSessionService.getSessionById(sessionId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get sessions by mentor ID", description = "Fetches all sessions associated with a specific mentor")
    @ApiResponse(responseCode = "200", description = "Sessions fetched successfully")
    @GetMapping("/mentor/{mentorId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<List<SessionResponseDto>> getSessionsByMentor(@PathVariable UUID mentorId) {
        List<SessionResponseDto> result = mentorshipSessionService.getSessionsByMentor(mentorId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get sessions by mentee ID", description = "Fetches all sessions associated with a specific mentee")
    @ApiResponse(responseCode = "200", description = "Sessions fetched successfully")
    @GetMapping("/mentee/{menteeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<List<SessionResponseDto>> getSessionsByMentee(@PathVariable UUID menteeId) {
        List<SessionResponseDto> result = mentorshipSessionService.getSessionsByMentee(menteeId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Update the status of a mentorship session", description = "Updates the session status (e.g., from PENDING to COMPLETED)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PutMapping("/{sessionId}/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<SessionResponseDto> updateSessionStatus(
            @PathVariable Long sessionId,
            @RequestParam String status) {
        SessionResponseDto result = mentorshipSessionService.updateSessionStatus(sessionId, status);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Delete a mentorship session", description = "Deletes a mentorship session by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<Void> deleteSession(@PathVariable Long sessionId) {
        mentorshipSessionService.deleteSession(sessionId);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
