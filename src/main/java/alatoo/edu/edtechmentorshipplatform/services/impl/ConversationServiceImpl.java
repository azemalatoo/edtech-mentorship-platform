package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.ConversationStatus;
import alatoo.edu.edtechmentorshipplatform.repo.ConversationRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepo conversationRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public Conversation createConversation(UUID mentorId, UUID menteeId) {
        User mentor = userRepo.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));
        User mentee = userRepo.findById(menteeId)
                .orElseThrow(() -> new IllegalArgumentException("Mentee not found"));
        Conversation conv = Conversation.builder()
                .mentor(mentor)
                .mentee(mentee)
                .status(ConversationStatus.OPEN)
                .isActive(Boolean.TRUE)
                .lastActiveAt(LocalDateTime.now())
                .build();
        return conversationRepo.save(conv);
    }

    @Override
    public List<Conversation> getConversationsForUser(UUID userId) {
        return conversationRepo.findByMentorIdOrMenteeId(userId, userId);
    }

    @Override
    @Transactional
    public Conversation closeConversation(Long conversationId) {
        Conversation conv = conversationRepo.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        conv.setStatus(ConversationStatus.CLOSED);
        conv.setIsActive(false);
        return conversationRepo.save(conv);
    }
}
