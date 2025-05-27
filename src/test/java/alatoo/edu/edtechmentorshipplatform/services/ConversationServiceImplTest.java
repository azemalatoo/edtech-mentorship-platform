package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.ConversationStatus;
import alatoo.edu.edtechmentorshipplatform.repo.ConversationRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.ConversationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationServiceImplTest {

    @Mock private ConversationRepo conversationRepo;
    @Mock private UserRepo userRepo;

    @InjectMocks private ConversationServiceImpl service;

    private User mentor;
    private User mentee;
    private UUID mentorId;
    private UUID menteeId;
    private Conversation convTemplate;

    @BeforeEach
    void setUp() {
        mentorId = UUID.randomUUID();
        menteeId = UUID.randomUUID();
        mentor = new User(); mentor.setId(mentorId);
        mentee = new User(); mentee.setId(menteeId);
        convTemplate = Conversation.builder()
                .id(42L)
                .mentor(mentor)
                .mentee(mentee)
                .status(ConversationStatus.OPEN)
                .isActive(true)
                .lastActiveAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createConversation_success() {
        when(userRepo.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(userRepo.findById(menteeId)).thenReturn(Optional.of(mentee));
        when(conversationRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Conversation result = service.createConversation(mentorId, menteeId);

        assertThat(result.getMentor()).isSameAs(mentor);
        assertThat(result.getMentee()).isSameAs(mentee);
        assertThat(result.getStatus()).isEqualTo(ConversationStatus.OPEN);
        assertThat(result.getIsActive()).isTrue();
        assertThat(result.getLastActiveAt()).isBeforeOrEqualTo(LocalDateTime.now());

        ArgumentCaptor<Conversation> cap = ArgumentCaptor.forClass(Conversation.class);
        verify(conversationRepo).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(ConversationStatus.OPEN);
    }

    @Test
    void createConversation_mentorNotFound() {
        when(userRepo.findById(mentorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createConversation(mentorId, menteeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mentor not found");

        verify(userRepo).findById(mentorId);
        verify(conversationRepo, never()).save(any());
    }

    @Test
    void createConversation_menteeNotFound() {
        when(userRepo.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(userRepo.findById(menteeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createConversation(mentorId, menteeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mentee not found");

        verify(userRepo).findById(menteeId);
        verify(conversationRepo, never()).save(any());
    }

    @Test
    void getConversationsForUser_delegates() {
        List<Conversation> list = List.of(convTemplate);
        when(conversationRepo.findByMentorIdOrMenteeId(mentorId, mentorId))
                .thenReturn(list);

        List<Conversation> result = service.getConversationsForUser(mentorId);
        assertThat(result).isSameAs(list);
        verify(conversationRepo).findByMentorIdOrMenteeId(mentorId, mentorId);
    }

    @Test
    void closeConversation_success() {
        when(conversationRepo.findById(42L)).thenReturn(Optional.of(convTemplate));
        when(conversationRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Conversation result = service.closeConversation(42L);
        assertThat(result.getStatus()).isEqualTo(ConversationStatus.CLOSED);
        assertThat(result.getIsActive()).isFalse();

        ArgumentCaptor<Conversation> cap = ArgumentCaptor.forClass(Conversation.class);
        verify(conversationRepo).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(ConversationStatus.CLOSED);
    }

    @Test
    void closeConversation_notFound() {
        when(conversationRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.closeConversation(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Conversation not found");
        verify(conversationRepo, never()).save(any());
    }
}
