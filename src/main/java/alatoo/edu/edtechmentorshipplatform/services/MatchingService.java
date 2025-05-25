package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileResponseDto;

import java.util.List;
import java.util.UUID;

public interface MatchingService {
    List<MentorProfileResponseDto> matchMentorsForMentee(UUID menteeProfileId);
}
