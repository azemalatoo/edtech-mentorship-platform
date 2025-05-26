package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentor.RejectRequestDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.services.AdminMentorService;
import alatoo.edu.edtechmentorshipplatform.services.MentorProfileService;
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
@RequestMapping("/api/v1/admin/mentors")
@Tag(name = "AdminMentorController", description = "APIs for admin mentor approval and management")
@RequiredArgsConstructor
public class AdminMentorController {

    private final AdminMentorService adminService;
    private final MentorProfileService mentorProfileService;

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseApi<List<MentorProfile>> listPending() {
        return new ResponseApi<>(adminService.listPending(), ResponseCode.SUCCESS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseApi<MentorProfile> getById(@PathVariable UUID id) {
        return new ResponseApi<>(adminService.getById(id), ResponseCode.SUCCESS);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseApi<MentorProfile> approve(@PathVariable UUID id) {
        return new ResponseApi<>(adminService.approve(id), ResponseCode.SUCCESS);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseApi<MentorProfile> reject(
        @PathVariable UUID id,
        @Valid @RequestBody RejectRequestDto dto) {
        return new ResponseApi<>(adminService.reject(id, dto.getReason()), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get all mentor profiles")
    @ApiResponse(responseCode = "200", description = "Mentor profiles retrieved successfully")
    @GetMapping
    public ResponseApi<List<MentorProfileResponseDto>> getAllMentorProfiles() {
        List<MentorProfileResponseDto> profiles = mentorProfileService.getAllMentorProfiles();
        return new ResponseApi<>(profiles, ResponseCode.SUCCESS);
    }
}
