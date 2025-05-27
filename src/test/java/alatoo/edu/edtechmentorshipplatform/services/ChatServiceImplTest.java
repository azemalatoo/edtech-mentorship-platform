package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ChatMessage;
import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.Message;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.MessageStatus;
import alatoo.edu.edtechmentorshipplatform.mapper.ChatMapper;
import alatoo.edu.edtechmentorshipplatform.repo.ConversationRepo;
import alatoo.edu.edtechmentorshipplatform.repo.MessageRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.ChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock private MessageRepo messageRepo;
    @Mock private ConversationRepo conversationRepo;
    @Mock private UserRepo userRepo;
    @Mock private ChatMapper chatMapper;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks private ChatServiceImpl service;

    private ChatMessage dto;
    private Conversation conv;
    private User sender;
    private User recipient;
    private Message entity;
    private ChatMessage mappedDto;

    @BeforeEach
    void setUp() {
        dto = new ChatMessage();
        dto.setConversationId(10L);
        dto.setSenderId(UUID.randomUUID());
        dto.setRecipientId(UUID.randomUUID());
        dto.setContent("Hello");

        conv = new Conversation();
        conv.setId(10L);

        sender = new User();
        sender.setId(dto.getSenderId());
        recipient = new User();
        recipient.setId(dto.getRecipientId());

        entity = new Message();

        mappedDto = new ChatMessage();
        mappedDto.setConversationId(10L);
        mappedDto.setContent("Hello");
    }

    @Test
    void sendMessage_success_shouldSaveAndPublish() {
        when(conversationRepo.findById(10L)).thenReturn(Optional.of(conv));
        when(userRepo.findById(dto.getSenderId())).thenReturn(Optional.of(sender));
        when(userRepo.findById(dto.getRecipientId())).thenReturn(Optional.of(recipient));
        when(chatMapper.toEntity(dto, conv, sender, recipient)).thenReturn(entity);

        entity.setId(99L);
        when(messageRepo.save(entity)).thenReturn(entity);
        when(chatMapper.toDto(entity)).thenReturn(mappedDto);

        ChatMessage result = service.sendMessage(dto);

        assertThat(result).isSameAs(mappedDto);

        ArgumentCaptor<ChatMessage> captor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(messagingTemplate).convertAndSend(eq("/topic/messages/10"), captor.capture());
        assertThat(captor.getValue()).isSameAs(mappedDto);

        assertThat(entity.getStatus()).isEqualTo(MessageStatus.SENT);
        assertThat(entity.getSentAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void sendMessage_missingConversation_shouldThrow() {
        when(conversationRepo.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.sendMessage(dto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Conversation not found");
    }

    @Test
    void sendMessage_missingSender_shouldThrow() {
        when(conversationRepo.findById(10L)).thenReturn(Optional.of(conv));
        when(userRepo.findById(dto.getSenderId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.sendMessage(dto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Sender not found");
    }

    @Test
    void sendMessage_missingRecipient_shouldThrow() {
        when(conversationRepo.findById(10L)).thenReturn(Optional.of(conv));
        when(userRepo.findById(dto.getSenderId())).thenReturn(Optional.of(sender));
        when(userRepo.findById(dto.getRecipientId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.sendMessage(dto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Recipient not found");
    }

    @Test
    void readReceipt_success_shouldUpdateAndPublish() {
        Message msg = new Message();
        msg.setId(5L);
        msg.setConversation(conv);
        msg.setStatus(MessageStatus.SENT);

        when(messageRepo.findById(5L)).thenReturn(Optional.of(msg));
        when(messageRepo.save(msg)).thenReturn(msg);
        when(chatMapper.toDto(msg)).thenReturn(mappedDto);

        ChatMessage result = service.readReceipt(5L);

        assertThat(result).isSameAs(mappedDto);
        assertThat(msg.getStatus()).isEqualTo(MessageStatus.READ);

        verify(messagingTemplate)
            .convertAndSend("/topic/messages/" + mappedDto.getConversationId(), mappedDto);
    }

    @Test
    void readReceipt_notFound_shouldThrow() {
        when(messageRepo.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.readReceipt(123L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Message not found: 123");
    }
}
