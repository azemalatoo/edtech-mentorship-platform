package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MentorProfileRepo extends JpaRepository<MentorProfile, UUID> {
    Optional<MentorProfile> findByUserId(UUID userId);

    List<MentorProfile> findByExpertiseCategoryId(UUID interestedCategoryId);
}
