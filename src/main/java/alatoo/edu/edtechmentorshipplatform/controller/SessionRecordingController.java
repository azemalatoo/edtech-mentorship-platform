package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.SessionRecordingService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session-recordings")
@Tag(name = "SessionRecordingController", description = "APIs for managing session recordings")
@RequiredArgsConstructor
public class SessionRecordingController {
    private final SessionRecordingService recordingService;

    @Operation(summary = "Upload a session recording", description = "Allows a mentor to upload a completed session's recording link.")
    @ApiResponse(responseCode = "201", description = "Recording uploaded successfully")
    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<SessionRecordingResponseDto> upload(
            @Valid @RequestBody SessionRecordingRequestDto request) {
        return new ResponseApi<>(
            recordingService.uploadRecording(request), ResponseCode.CREATED
        );
    }

    @Operation(summary = "Get recordings by session", description = "Retrieves all recordings for a specific mentorship session.")
    @ApiResponse(responseCode = "200", description = "Recordings retrieved successfully")
    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<List<SessionRecordingResponseDto>> getBySession(
            @PathVariable Long sessionId) {
        return new ResponseApi<>(
            recordingService.getRecordingsBySession(sessionId), ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Get recordings for current user", description = "Retrieves all session recordings accessible by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Recordings retrieved successfully")
    @GetMapping("/byCurrentUser")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<List<SessionRecordingResponseDto>> getForCurrentUser() {
        return new ResponseApi<>(
            recordingService.getRecordingsForCurrentUser(), ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Delete a session recording", description = "Allows a mentor to delete a recording they uploaded.")
    @ApiResponse(responseCode = "200", description = "Recording deleted successfully")
    @DeleteMapping("/{recordingId}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<Void> delete(@PathVariable Long recordingId) {
        recordingService.deleteRecording(recordingId);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
