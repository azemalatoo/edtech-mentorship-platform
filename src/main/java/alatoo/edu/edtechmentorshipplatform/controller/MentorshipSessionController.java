package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.session.MentorshipSessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionSlotRequestDto;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import alatoo.edu.edtechmentorshipplatform.services.MentorshipSessionService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@Tag(name = "MentorshipSessionController", description = "APIs for managing mentorship sessions")
@RequiredArgsConstructor
public class MentorshipSessionController {
    private final MentorshipSessionService sessionService;

    @Operation(summary = "Create a new available slot")
    @ApiResponse(responseCode = "201", description = "Slot created successfully")
    @PostMapping("/slots")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<MentorshipSessionResponseDto> createSlot(
            @Valid @RequestBody SessionSlotRequestDto request) {
        return new ResponseApi<>(
                sessionService.createSlot(request),
                ResponseCode.CREATED
        );
    }

    @Operation(summary = "Update an available slot")
    @ApiResponse(responseCode = "200", description = "Slot updated successfully")
    @PutMapping("/slots/{slotId}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<MentorshipSessionResponseDto> updateSlot(
            @PathVariable Long slotId,
            @Valid @RequestBody SessionSlotRequestDto request) {
        return new ResponseApi<>(
                sessionService.updateSlot(slotId, request),
                ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Get available slots for a mentor")
    @GetMapping("/slots/mentor/{mentorId}")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<List<MentorshipSessionResponseDto>> getSlotsByMentorAndStatus(
            @PathVariable UUID mentorId,
            @RequestParam SessionStatus status) {
        return new ResponseApi<>(
                sessionService.getSlotsByMentorAndStatus(mentorId, status),
                ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Book an available slot")
    @PostMapping("/{slotId}/book")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<MentorshipSessionResponseDto> bookSlot(
            @PathVariable Long slotId) {
        return new ResponseApi<>(
                sessionService.bookSlot(slotId),
                ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Start a booked session")
    @PostMapping("/{sessionId}/start")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<MentorshipSessionResponseDto> startSession(
            @PathVariable Long sessionId) {
        return new ResponseApi<>(
                sessionService.startSession(sessionId),
                ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Complete an in-progress session")
    @PostMapping("/{sessionId}/complete")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseApi<MentorshipSessionResponseDto> completeSession(
            @PathVariable Long sessionId) {
        return new ResponseApi<>(
                sessionService.completeSession(sessionId),
                ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Cancel a session before it starts")
    @PostMapping("/{sessionId}/cancel")
    @PreAuthorize("hasAnyRole('MENTOR','MENTEE')")
    public ResponseApi<MentorshipSessionResponseDto> cancelSession(
            @PathVariable Long sessionId) {
        return new ResponseApi<>(
                sessionService.cancelSession(sessionId),
                ResponseCode.SUCCESS
        );
    }

    @Operation(summary = "Get sessions for the authenticated mentee by status")
    @GetMapping("/mentee")
    @PreAuthorize("hasRole('MENTEE')")
    public ResponseApi<List<MentorshipSessionResponseDto>> getSessionsByMentee(@RequestParam(required = false) SessionStatus status) {
        return new ResponseApi<>(
                sessionService.getSessionsByMentee(status),
                ResponseCode.SUCCESS
        );
    }
}
