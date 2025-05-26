package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import alatoo.edu.edtechmentorshipplatform.services.MenteeProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('MENTEE')")
@RestController
@RequestMapping("/api/v1/mentee-profiles")
@RequiredArgsConstructor
@Tag(name = "MenteeProfileController", description = "APIs for managing mentee profiles")
public class MenteeProfileController {

    private final MenteeProfileService menteeProfileService;

    @Operation(summary = "Create a new mentee profile", description = "Create a new profile for a mentee.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<MenteeProfileResponseDto> createProfile(
            @Valid @RequestBody MenteeProfileRequestDto dto) {
        MenteeProfileResponseDto result = menteeProfileService.createProfile(dto);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @Operation(summary = "Update an existing mentee profile", description = "Update the details of a mentee profile by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @PutMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<MenteeProfileResponseDto> updateProfile(
            @PathVariable UUID profileId,
            @Valid @RequestBody MenteeProfileRequestDto dto) {
        MenteeProfileResponseDto result = menteeProfileService.updateProfile(profileId, dto);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get a mentee profile by ID", description = "Retrieve a mentee profile by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<MenteeProfileResponseDto> getProfileById(@PathVariable UUID profileId) {
        MenteeProfileResponseDto result = menteeProfileService.getProfileById(profileId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get all mentee profiles", description = "Retrieve a list of all mentee profiles.")
    @ApiResponse(responseCode = "200", description = "Profiles fetched successfully")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<List<MenteeProfileResponseDto>> getAllProfiles() {
        List<MenteeProfileResponseDto> result = menteeProfileService.getAllProfiles();
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Delete a mentee profile by ID", description = "Delete the mentee profile based on the provided ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/{profileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<Void> deleteProfile(@PathVariable UUID profileId) {
        menteeProfileService.deleteProfile(profileId);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
