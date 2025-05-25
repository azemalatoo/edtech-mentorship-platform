package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.RejectRequestDto;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.services.AdminMentorService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/mentors")
@RequiredArgsConstructor
public class AdminMentorController {

    private final AdminMentorService adminService;

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
}
