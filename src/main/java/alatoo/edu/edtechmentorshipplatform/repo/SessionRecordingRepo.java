package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.SessionRecording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRecordingRepo extends JpaRepository<SessionRecording, Long> {
}
