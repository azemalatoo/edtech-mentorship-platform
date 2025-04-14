package org.example.edtechmentorshipplatform.repo;

import org.example.edtechmentorshipplatform.entity.MenteeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenteeProfileRepo extends JpaRepository<MenteeProfile, Long> {
}
