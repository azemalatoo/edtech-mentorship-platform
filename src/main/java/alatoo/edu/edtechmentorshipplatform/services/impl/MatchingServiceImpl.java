package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.users.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.MentorProfileMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MenteeProfileRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MentorProfileRepo;
import alatoo.edu.edtechmentorshipplatform.services.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final MenteeProfileRepo menteeProfileRepository;
    private final MentorProfileRepo mentorProfileRepository;

    @Override
    public List<MentorProfileResponseDto> matchMentorsForMentee(UUID menteeProfileId) {
        MenteeProfile menteeProfile = menteeProfileRepository.findById(menteeProfileId)
                .orElseThrow(() -> new NotFoundException("Mentee profile not found"));

        UUID interestedCategoryId = menteeProfile.getInterestedCategory().getId();
        Lang preferredLanguage = menteeProfile.getPreferredLanguage();

        List<MentorProfile> mentorsInCategory = mentorProfileRepository.findByExpertiseCategoryId(interestedCategoryId);

        List<MentorProfile> matchedMentors = mentorsInCategory.stream()
                .filter(mentor -> mentor.getLanguages().contains(preferredLanguage))
                .collect(Collectors.toList());

        return matchedMentors.stream()
                .map(MentorProfileMapper::toDto)
                .collect(Collectors.toList());
    }
}
