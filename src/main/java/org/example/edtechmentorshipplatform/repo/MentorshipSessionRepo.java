package org.example.edtechmentorshipplatform.repo;

import org.example.edtechmentorshipplatform.entity.MentorshipSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorshipSessionRepo extends JpaRepository<MentorshipSession, Long> {
}
