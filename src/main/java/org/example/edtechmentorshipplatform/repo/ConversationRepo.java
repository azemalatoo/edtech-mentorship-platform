package org.example.edtechmentorshipplatform.repo;

import org.example.edtechmentorshipplatform.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, Long> {
}
