package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.controller.base.BaseRestController;
import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.MentorProfileService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/mentor-profile")
@RequiredArgsConstructor
@Tag(name = "MenteeProfileController", description = "APIs for managing mentor profiles")
public class MentorProfileController extends BaseRestController {

    private final MentorProfileService mentorProfileService;

    @Operation(summary = "Get mentor profile by ID", description = "Fetches a mentor's profile using their profile ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<MentorProfileResponseDto> getProfileById(
            @Parameter(description = "Mentor Profile ID") @PathVariable UUID profileId) {
        MentorProfileResponseDto result = mentorProfileService.getProfileById(profileId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Update mentor profile", description = "Updates a mentor's profile with the given details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Profile or User not found")
    })
    @PutMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<MentorProfileResponseDto> updateProfile(
            @Parameter(description = "Mentor Profile ID") @PathVariable UUID profileId,
            @Valid @RequestBody MentorProfileRequestDto dto) {
        MentorProfileResponseDto result = mentorProfileService.updateProfile(profileId, dto);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Create mentor profile", description = "Creates a new mentor profile with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<MentorProfileResponseDto> createProfile(
            @Valid @RequestBody MentorProfileRequestDto dto) {
        MentorProfileResponseDto result = mentorProfileService.createMentorProfile(dto);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @Operation(summary = "Delete mentor profile", description = "Deletes a mentor's profile by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<Void> deleteProfile(
            @Parameter(description = "Mentor Profile ID") @PathVariable UUID profileId) {
        mentorProfileService.deleteMentorProfile(profileId);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get mentor profile by user ID", description = "Fetches a mentor's profile by user ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile or User not found")
    })
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<MentorProfileResponseDto> getProfileByUserId(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        MentorProfileResponseDto result = mentorProfileService.getMentorProfileByUserId(userId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Approve mentor profile", description = "Approve a pending mentor profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mentor profile approved successfully"),
            @ApiResponse(responseCode = "404", description = "Mentor profile not found")
    })
    @PutMapping("/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<MentorProfileResponseDto> approveProfile(
            @Parameter(description = "Mentor Profile ID") @PathVariable UUID id) {
        MentorProfileResponseDto result = mentorProfileService.approveMentorProfile(id);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }
}
