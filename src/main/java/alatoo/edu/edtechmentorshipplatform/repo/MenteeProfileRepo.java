package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.MenteeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenteeProfileRepo extends JpaRepository<MenteeProfile, UUID> {
    Optional<MenteeProfile> findByUserId(UUID userId);
}
