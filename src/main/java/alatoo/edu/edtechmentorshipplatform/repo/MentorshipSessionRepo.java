package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.MentorshipSession;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MentorshipSessionRepo extends JpaRepository<MentorshipSession, Long> {
    List<MentorshipSession> findByMentorIdAndStatus(UUID mentorId, SessionStatus status);
    List<MentorshipSession> findByMenteeIdAndStatus(UUID menteeId, SessionStatus status);
    List<MentorshipSession> findByMentorIdAndAvailableFromBetween(UUID mentorId, LocalDateTime from, LocalDateTime to);
    List<MentorshipSession> findByMenteeId(UUID mentorId);
}