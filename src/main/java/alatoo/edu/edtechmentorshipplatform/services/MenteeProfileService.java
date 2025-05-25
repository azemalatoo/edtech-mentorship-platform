package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileResponseDto;
import java.util.List;
import java.util.UUID;

public interface MenteeProfileService {
    MenteeProfileResponseDto createProfile(MenteeProfileRequestDto menteeProfileRequestDto);
    MenteeProfileResponseDto updateProfile(UUID profileId, MenteeProfileRequestDto menteeProfileRequestDto);
    MenteeProfileResponseDto getProfileById(UUID profileId);
    void deleteProfile(UUID profileId);
    List<MenteeProfileResponseDto> getAllProfiles();
}
