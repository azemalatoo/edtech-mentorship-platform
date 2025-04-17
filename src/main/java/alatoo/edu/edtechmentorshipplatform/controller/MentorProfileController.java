package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.MentorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mentor-profile")
public class MentorProfileController {

    private final MentorProfileService mentorProfileService;

    @Autowired
    public MentorProfileController(MentorProfileService mentorProfileService) {
        this.mentorProfileService = mentorProfileService;
    }

    @Operation(summary = "Get mentor profile by ID", description = "Fetches a mentor's profile using their profile ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/{profileId}")
    public ResponseEntity<MentorProfileResponseDto> getProfileById(
            @Parameter(description = "Mentor Profile ID") @PathVariable UUID profileId) {
        MentorProfileResponseDto response = mentorProfileService.getProfileById(profileId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update mentor profile", description = "Updates a mentor's profile with the given details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Profile or User not found")
    })
    @PutMapping("/{profileId}")
    public ResponseEntity<MentorProfileResponseDto> updateProfile(
            @Parameter(description = "Mentor Profile ID") @PathVariable UUID profileId,
            @RequestBody MentorProfileRequestDto mentorProfileRequestDto) {
        MentorProfileResponseDto response = mentorProfileService.updateProfile(profileId, mentorProfileRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create mentor profile", description = "Creates a new mentor profile with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<MentorProfileResponseDto> createProfile(
            @RequestBody MentorProfileRequestDto mentorProfileRequestDto) {
        MentorProfileResponseDto response = mentorProfileService.createMentorProfile(mentorProfileRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete mentor profile", description = "Deletes a mentor's profile by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(
            @Parameter(description = "Mentor Profile ID") @PathVariable UUID profileId) {
        mentorProfileService.deleteMentorProfile(profileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get mentor profile by user ID", description = "Fetches a mentor's profile by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile or User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<MentorProfileResponseDto> getProfileByUserId(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        MentorProfileResponseDto response = mentorProfileService.getMentorProfileByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "Approve mentor profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mentor profile approved successfully"),
            @ApiResponse(responseCode = "404", description = "Mentor profile not found")
    })
    @PutMapping("/{id}/approve")
    public ResponseEntity<MentorProfileResponseDto> approveProfile(@PathVariable UUID id) {
        MentorProfileResponseDto approvedProfile = mentorProfileService.approveMentorProfile(id);
        return new ResponseEntity<>(approvedProfile, HttpStatus.OK);
    }
}
