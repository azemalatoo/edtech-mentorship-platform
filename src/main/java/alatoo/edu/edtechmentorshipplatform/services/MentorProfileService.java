package alatoo.edu.edtechmentorshipplatform.services;


import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileResponseDto;

import java.util.List;
import java.util.UUID;

public interface MentorProfileService {
    MentorProfileResponseDto getMentorProfileByUserId(UUID userId);
    MentorProfileResponseDto createMentorProfile(MentorProfileRequestDto dto);
    MentorProfileResponseDto updateProfile(UUID profileId, MentorProfileRequestDto dto);
    MentorProfileResponseDto getProfileById(UUID profileId);
    void deleteMentorProfile(UUID id);
    MentorProfileResponseDto approveMentorProfile(UUID profileId);
    List<MentorProfileResponseDto> getAllMentorProfiles();
}
