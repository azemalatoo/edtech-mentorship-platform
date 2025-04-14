package org.example.edtechmentorshipplatform.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorProfileRepo extends JpaRepository<MentorProfileRepo, Long> {
}
