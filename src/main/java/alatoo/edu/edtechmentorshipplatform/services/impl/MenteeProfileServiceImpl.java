package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.mentee.MenteeProfileResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.mapper.MenteeProfileMapper;
import alatoo.edu.edtechmentorshipplatform.repo.MenteeProfileRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.MenteeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MenteeProfileServiceImpl implements MenteeProfileService {

    private final MenteeProfileRepo menteeProfileRepo;
    private final UserRepo userRepo;

    @Autowired
    public MenteeProfileServiceImpl(MenteeProfileRepo menteeProfileRepo, UserRepo userRepo) {
        this.menteeProfileRepo = menteeProfileRepo;
        this.userRepo = userRepo;
    }

    @Override
    public MenteeProfileResponseDto createProfile(MenteeProfileRequestDto menteeProfileRequestDto) {
        User user = userRepo.findById(menteeProfileRequestDto.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MenteeProfile menteeProfile = new MenteeProfile();
        menteeProfile.setUser(user);
        menteeProfile.setEducationLevel(menteeProfileRequestDto.getEducationLevel());
        menteeProfile.setFieldOfStudy(menteeProfileRequestDto.getFieldOfStudy());
        menteeProfile.setCareerGoal(menteeProfileRequestDto.getCareerGoal());
        menteeProfile.setPreferredLanguage(menteeProfileRequestDto.getPreferredLanguage());

        menteeProfile = menteeProfileRepo.save(menteeProfile);

        return MenteeProfileMapper.toDto(menteeProfile);
    }

    @Override
    public MenteeProfileResponseDto updateProfile(UUID profileId, MenteeProfileRequestDto menteeProfileRequestDto) {
        MenteeProfile menteeProfile = menteeProfileRepo.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        menteeProfile.setEducationLevel(menteeProfileRequestDto.getEducationLevel());
        menteeProfile.setFieldOfStudy(menteeProfileRequestDto.getFieldOfStudy());
        menteeProfile.setCareerGoal(menteeProfileRequestDto.getCareerGoal());
        menteeProfile.setPreferredLanguage(menteeProfileRequestDto.getPreferredLanguage());

        menteeProfile = menteeProfileRepo.save(menteeProfile);

        return MenteeProfileMapper.toDto(menteeProfile);
    }

    @Override
    public MenteeProfileResponseDto getProfileById(UUID profileId) {
        MenteeProfile menteeProfile = menteeProfileRepo.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        return MenteeProfileMapper.toDto(menteeProfile);
    }

    @Override
    public void deleteProfile(UUID profileId) {
        MenteeProfile menteeProfile = menteeProfileRepo.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        menteeProfileRepo.delete(menteeProfile);
    }

    @Override
    public List<MenteeProfileResponseDto> getAllProfiles() {
        List<MenteeProfile> menteeProfiles = menteeProfileRepo.findAll();
        return menteeProfiles.stream()
                .map(MenteeProfileMapper::toDto)
                .collect(Collectors.toList());
    }
}
