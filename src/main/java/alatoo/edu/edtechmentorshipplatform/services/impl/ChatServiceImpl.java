package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ChatMessage;
import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.Message;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.MessageStatus;
import alatoo.edu.edtechmentorshipplatform.mapper.ChatMapper;
import alatoo.edu.edtechmentorshipplatform.repo.ConversationRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MessageRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MessageRepo messageRepo;
    private final ConversationRepo conversationRepo;
    private final UserRepo userRepo;
    private final ChatMapper chatMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        Conversation conv = conversationRepo.findById(chatMessage.getConversationId())
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        User sender = userRepo.findById(chatMessage.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User recipient = userRepo.findById(chatMessage.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        Message entity = chatMapper.toEntity(chatMessage, conv, sender, recipient);
        entity.setSentAt(LocalDateTime.now());
        entity.setStatus(MessageStatus.SENT);

        Message saved = messageRepo.save(entity);

        ChatMessage dto = chatMapper.toDto(saved);

        messagingTemplate.convertAndSend(
            "/topic/messages/" + dto.getConversationId(), dto
        );
        return dto;
    }

    @Override
    @Transactional
    public ChatMessage readReceipt(Long messageId) {
        Message msg = messageRepo.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));
        msg.setStatus(MessageStatus.READ);
        Message updated = messageRepo.save(msg);
        ChatMessage dto = chatMapper.toDto(updated);
        messagingTemplate.convertAndSend(
            "/topic/messages/" + dto.getConversationId(), dto
        );
        return dto;
    }
}