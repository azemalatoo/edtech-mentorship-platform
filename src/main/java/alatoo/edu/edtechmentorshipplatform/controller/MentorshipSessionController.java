package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.session.SessionRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.session.SessionResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.MentorshipSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mentorship-sessions")
public class MentorshipSessionController {

    private final MentorshipSessionService mentorshipSessionService;

    @Autowired
    public MentorshipSessionController(MentorshipSessionService mentorshipSessionService) {
        this.mentorshipSessionService = mentorshipSessionService;
    }

    @Operation(summary = "Create a new mentorship session", description = "Creates a new session for a mentor and mentee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<SessionResponseDto> createSession(@RequestBody SessionRequestDto createSessionRequestDto) {
        SessionResponseDto createdSession = mentorshipSessionService.createSession(createSessionRequestDto);
        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a mentorship session by ID", description = "Fetches the details of a session by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponseDto> getSessionById(@PathVariable UUID sessionId) {
        SessionResponseDto session = mentorshipSessionService.getSessionById(sessionId);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @Operation(summary = "Get sessions by mentor ID", description = "Fetches all sessions associated with a specific mentor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessions fetched successfully"),
    })
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<SessionResponseDto>> getSessionsByMentor(@PathVariable UUID mentorId) {
        List<SessionResponseDto> sessions = mentorshipSessionService.getSessionsByMentor(mentorId);
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @Operation(summary = "Get sessions by mentee ID", description = "Fetches all sessions associated with a specific mentee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessions fetched successfully"),
    })
    @GetMapping("/mentee/{menteeId}")
    public ResponseEntity<List<SessionResponseDto>> getSessionsByMentee(@PathVariable UUID menteeId) {
        List<SessionResponseDto> sessions = mentorshipSessionService.getSessionsByMentee(menteeId);
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }

    @Operation(summary = "Update the status of a mentorship session", description = "Updates the session status (e.g., from PENDING to COMPLETED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PutMapping("/{sessionId}/status")
    public ResponseEntity<SessionResponseDto> updateSessionStatus(@PathVariable UUID sessionId,
                                                                  @RequestParam String status) {
        SessionResponseDto updatedSession = mentorshipSessionService.updateSessionStatus(sessionId, status);
        return new ResponseEntity<>(updatedSession, HttpStatus.OK);
    }

    @Operation(summary = "Delete a mentorship session", description = "Deletes a mentorship session by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID sessionId) {
        mentorshipSessionService.deleteSession(sessionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
