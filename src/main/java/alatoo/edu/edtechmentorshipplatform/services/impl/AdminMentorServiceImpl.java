package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.enums.ProfileStatus;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.repo.MentorProfileRepo;
import alatoo.edu.edtechmentorshipplatform.services.AdminMentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMentorServiceImpl implements AdminMentorService {
    private final MentorProfileRepo repo;

    @Override
    public List<MentorProfile> listPending() {
        return repo.findByProfileStatus(ProfileStatus.PENDING);
    }

    @Override
    public MentorProfile getById(UUID id) {
        return repo.findById(id)
            .orElseThrow(() -> new NotFoundException("Mentor profile not found"));
    }

    @Override
    public MentorProfile approve(UUID id) {
        MentorProfile p = getById(id);
        p.setProfileStatus(ProfileStatus.APPROVED);
        return repo.save(p);
    }

    @Override
    public MentorProfile reject(UUID id, String reason) {
        MentorProfile p = getById(id);
        p.setProfileStatus(ProfileStatus.REJECTED);
        return repo.save(p);
    }
}