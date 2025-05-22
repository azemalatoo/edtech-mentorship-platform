package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ConversationResponseDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.chat.MessageResponseDto;
import alatoo.edu.edtechmentorshipplatform.entity.Message;
import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.ConversationStatus;
import alatoo.edu.edtechmentorshipplatform.mapper.MessageMapper;
import alatoo.edu.edtechmentorshipplatform.mapper.ConversationMapper;
import alatoo.edu.edtechmentorshipplatform.enums.MessageStatus;
import alatoo.edu.edtechmentorshipplatform.repo.ConversationRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MessageRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ConversationRepo conversationRepository;

    @Autowired
    private MessageRepo messageRepository;

    @Autowired
    private UserRepo userRepository;

    @Override
    public ConversationResponseDto createConversation(UUID mentorId, UUID menteeId, String conversationType) {
        User mentor = userRepository.findById(mentorId).orElseThrow(() -> new RuntimeException("Mentor not found"));
        User mentee = userRepository.findById(menteeId).orElseThrow(() -> new RuntimeException("Mentee not found"));

        Conversation conversation = new Conversation();
        conversation.setMentor(mentor);
        conversation.setMentee(mentee);
        conversation.setConversationType(conversationType);
        conversation.setStatus(ConversationStatus.ACTIVE);

        conversationRepository.save(conversation);

        return ConversationMapper.toDto(conversation);
    }

    @Override
    public MessageResponseDto sendMessage(Long conversationId, UUID senderId, UUID recipientId, MessageRequestDto messageRequestDto) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new RuntimeException("Conversation not found"));
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new RuntimeException("Recipient not found"));

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(messageRequestDto.getContent());
        message.setStatus(MessageStatus.SENT);

        messageRepository.save(message);

        return MessageMapper.toDto(message);
    }

    @Override
    public List<MessageResponseDto> getMessagesByConversation(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);
        return messages.stream()
                .map(MessageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }

    @Override
    public List<ConversationResponseDto> getConversationsByUser(UUID userId) {
        List<Conversation> conversations = conversationRepository.findByMentorIdOrMenteeId(userId, userId);
        return conversations.stream()
                .map(ConversationMapper::toDto)
                .collect(Collectors.toList());
    }
}
