package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, Long> {
    List<Conversation> findByMentorIdOrMenteeId(UUID mentorId, UUID menteeId);
}
