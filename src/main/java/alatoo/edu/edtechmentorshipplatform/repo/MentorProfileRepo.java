package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import alatoo.edu.edtechmentorshipplatform.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MentorProfileRepo extends JpaRepository<MentorProfile, UUID>, JpaSpecificationExecutor<MentorProfile> {
    Optional<MentorProfile> findByUserId(UUID userId);

    List<MentorProfile> findByExpertiseCategoryId(Long interestedCategoryId);

    List<MentorProfile> findByProfileStatus(ProfileStatus profileStatus);
}