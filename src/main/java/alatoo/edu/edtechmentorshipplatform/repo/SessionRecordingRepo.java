package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.SessionRecording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRecordingRepo extends JpaRepository<SessionRecording, Long> {
    List<SessionRecording> findBySessionId(Long sessionId);
    List<SessionRecording> findAllBySession_MenteeId(UUID menteeId);
    List<SessionRecording> findAllBySession_MentorId(UUID mentorId);
}
