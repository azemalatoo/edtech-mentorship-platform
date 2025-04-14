package org.example.edtechmentorshipplatform.repo;

import org.example.edtechmentorshipplatform.entity.SessionRecording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRecordingRepo extends JpaRepository<SessionRecording, Long> {
}
