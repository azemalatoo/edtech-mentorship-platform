package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.MatchingService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @Operation(summary = "Match mentors for a given mentee")
    @GetMapping("/mentee/{menteeProfileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<List<MentorProfileResponseDto>> matchMentors(
            @PathVariable UUID menteeProfileId) {
        List<MentorProfileResponseDto> result =
                matchingService.matchMentorsForMentee(menteeProfileId);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }
}
