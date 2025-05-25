package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRecordingResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.SessionRecordingService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
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

    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<SessionRecordingResponseDto> upload(
            @Valid @RequestBody SessionRecordingRequestDto request) {
        return new ResponseApi<>(
            recordingService.uploadRecording(request), ResponseCode.CREATED
        );
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<List<SessionRecordingResponseDto>> getBySession(
            @PathVariable Long sessionId) {
        return new ResponseApi<>(
            recordingService.getRecordingsBySession(sessionId), ResponseCode.SUCCESS
        );
    }

    @GetMapping("/byCurrentUser")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<List<SessionRecordingResponseDto>> getForCurrentUser() {
        return new ResponseApi<>(
            recordingService.getRecordingsForCurrentUser(), ResponseCode.SUCCESS
        );
    }

    @DeleteMapping("/{recordingId}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<Void> delete(@PathVariable Long recordingId) {
        recordingService.deleteRecording(recordingId);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
