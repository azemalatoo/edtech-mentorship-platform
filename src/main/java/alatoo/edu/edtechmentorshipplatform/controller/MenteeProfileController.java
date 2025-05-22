package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.users.MenteeProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.users.MenteeProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.MenteeProfileService;
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
@RequestMapping("/api/mentee-profiles")
public class MenteeProfileController {

    private final MenteeProfileService menteeProfileService;

    @Autowired
    public MenteeProfileController(MenteeProfileService menteeProfileService) {
        this.menteeProfileService = menteeProfileService;
    }

    @Operation(summary = "Create a new mentee profile", description = "Create a new profile for a mentee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<MenteeProfileResponseDto> createProfile(@RequestBody MenteeProfileRequestDto menteeProfileRequestDto) {
        MenteeProfileResponseDto createdProfile = menteeProfileService.createProfile(menteeProfileRequestDto);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing mentee profile", description = "Update the details of a mentee profile by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @PutMapping("/{profileId}")
    public ResponseEntity<MenteeProfileResponseDto> updateProfile(@PathVariable UUID profileId, @RequestBody MenteeProfileRequestDto menteeProfileRequestDto) {
        MenteeProfileResponseDto updatedProfile = menteeProfileService.updateProfile(profileId, menteeProfileRequestDto);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @Operation(summary = "Get a mentee profile by ID", description = "Retrieve a mentee profile by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/{profileId}")
    public ResponseEntity<MenteeProfileResponseDto> getProfileById(@PathVariable UUID profileId) {
        MenteeProfileResponseDto profile = menteeProfileService.getProfileById(profileId);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @Operation(summary = "Delete a mentee profile by ID", description = "Delete the mentee profile based on the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID profileId) {
        menteeProfileService.deleteProfile(profileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all mentee profiles", description = "Retrieve a list of all mentee profiles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<MenteeProfileResponseDto>> getAllProfiles() {
        List<MenteeProfileResponseDto> profiles = menteeProfileService.getAllProfiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }
}
