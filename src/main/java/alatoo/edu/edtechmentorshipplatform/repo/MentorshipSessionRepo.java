package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MentorshipSessionRepo extends JpaRepository<MentorshipSession, Long> {
    List<MentorshipSession> findByMenteeId(UUID menteeId);

    List<MentorshipSession> findByMentorId(UUID mentorId);
}
