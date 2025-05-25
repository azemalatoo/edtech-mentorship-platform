package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentor.MentorProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;
import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.ProfileStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.MentorProfileMapper;
import alatoo.edu.edtechmentorshipplatform.repo.CategoryRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MentorProfileRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.MentorProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MentorProfileServiceImpl implements MentorProfileService {

    private final MentorProfileRepo mentorProfileRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;


    @Override
    public MentorProfileResponseDto getMentorProfileByUserId(UUID userId) {
        MentorProfile mentorProfile = mentorProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Mentor profile not found for user id " + userId));
        return MentorProfileMapper.toDto(mentorProfile);
    }

    @Override
    @Transactional
    public MentorProfileResponseDto createMentorProfile(MentorProfileRequestDto dto) {
        User user = userRepo.findById(dto.getUserID())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + dto.getUserID()));

        Category category = categoryRepo.findById(dto.getExpertiseCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with ID: " + dto.getExpertiseCategoryId()));

        MentorProfile profile = MentorProfileMapper.toEntity(dto, user, category);
        profile.setProfileStatus(ProfileStatus.PENDING);
        MentorProfile savedProfile = mentorProfileRepo.save(profile);

        return MentorProfileMapper.toDto(savedProfile);
    }

    @Override
    @Transactional
    public MentorProfileResponseDto approveMentorProfile(UUID profileId) {
        MentorProfile profile = mentorProfileRepo.findById(profileId)
                .orElseThrow(() -> new NotFoundException("Mentor Profile not found with ID: " + profileId));

        profile.setProfileStatus(ProfileStatus.APPROVED);
        MentorProfile updated = mentorProfileRepo.save(profile);

        return MentorProfileMapper.toDto(updated);
    }

    @Override
    public List<MentorProfileResponseDto> getAllMentorProfiles() {
        return mentorProfileRepo.findAll().stream()
                .map(MentorProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMentorProfile(UUID id) {
        Optional<MentorProfile> mentorProfile = mentorProfileRepo.findById(id);
        mentorProfile.ifPresent(mentorProfileRepo::delete);
    }

    @Override
    public MentorProfileResponseDto updateProfile(UUID profileId, MentorProfileRequestDto dto) {
        Optional<MentorProfile> optionalProfile = mentorProfileRepo.findById(profileId);
        if (optionalProfile.isEmpty()) {
            throw new RuntimeException("Mentor profile not found");
        }
        MentorProfile existingProfile = optionalProfile.get();

        Optional<User> optionalUser = userRepo.findById(existingProfile.getUser().getId());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        Optional<Category> optionalCategory = categoryRepo.findById(dto.getExpertiseCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        Category category = optionalCategory.get();

        MentorProfile updatedProfile = MentorProfileMapper.toEntity(dto, user, category);
        updatedProfile.setId(profileId);
        MentorProfile savedProfile = mentorProfileRepo.save(updatedProfile);
        return MentorProfileMapper.toDto(savedProfile);
    }

    @Override
    public MentorProfileResponseDto getProfileById(UUID profileId) {
        Optional<MentorProfile> mentorProfile = mentorProfileRepo.findById(profileId);

        if (mentorProfile.isEmpty()) {
            throw new RuntimeException("Mentor profile not found");
        }

        return MentorProfileMapper.toDto(mentorProfile.get());
    }
}
