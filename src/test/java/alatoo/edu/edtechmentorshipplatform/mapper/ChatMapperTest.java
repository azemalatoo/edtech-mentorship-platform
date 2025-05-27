package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.chat.ChatMessage;
import alatoo.edu.edtechmentorshipplatform.entity.Conversation;
import alatoo.edu.edtechmentorshipplatform.entity.Message;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.MessageStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ChatMapperTest {

    private final ChatMapper mapper = new ChatMapper();

    @Test
    void toEntity_withoutAttachment_shouldMapFieldsAndSetTextType() {
        ChatMessage dto = new ChatMessage();
        dto.setContent("Hello, world!");
        dto.setAttachmentUrl(null);

        Conversation conv = Conversation.builder().id(1L).build();
        User sender    = User.builder().id(UUID.randomUUID()).build();
        User recipient = User.builder().id(UUID.randomUUID()).build();

        Message msg = mapper.toEntity(dto, conv, sender, recipient);

        assertThat(msg).isNotNull();
        assertThat(msg.getConversation()).isSameAs(conv);
        assertThat(msg.getSender()).isSameAs(sender);
        assertThat(msg.getRecipient()).isSameAs(recipient);
        assertThat(msg.getContent()).isEqualTo("Hello, world!");
        assertThat(msg.getAttachmentUrl()).isNull();
        assertThat(msg.isImportant()).isFalse();
        assertThat(msg.getMessageType()).isEqualTo("TEXT");
    }

    @Test
    void toEntity_withAttachment_shouldSetAttachmentType() {
        // given
        ChatMessage dto = new ChatMessage();
        dto.setContent("See this file");
        dto.setAttachmentUrl("http://example.com/file.png");

        Conversation conv = Conversation.builder().id(2L).build();
        User sender    = User.builder().id(UUID.randomUUID()).build();
        User recipient = User.builder().id(UUID.randomUUID()).build();

        Message msg = mapper.toEntity(dto, conv, sender, recipient);

        assertThat(msg.getContent()).isEqualTo("See this file");
        assertThat(msg.getAttachmentUrl()).isEqualTo("http://example.com/file.png");
        assertThat(msg.getMessageType()).isEqualTo("ATTACHMENT");
        assertThat(msg.isImportant()).isFalse();
    }

    @Test
    void toDto_shouldMapAllFields() {
        // given
        UUID senderId    = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        long convId      = 3L;

        Conversation conv = Conversation.builder().id(convId).build();
        User sender    = User.builder().id(senderId).build();
        User recipient = User.builder().id(recipientId).build();

        Message entity = Message.builder()
                .conversation(conv)
                .sender(sender)
                .recipient(recipient)
                .content("Hola!")
                .attachmentUrl("http://img.jpg")
                .isImportant(true)
                .messageType("ATTACHMENT")
                .build();
        entity.setId(99L);
        entity.setStatus(MessageStatus.SENT);
        LocalDateTime now = LocalDateTime.now();
        entity.setSentAt(now);

        ChatMessage dto = mapper.toDto(entity);

        assertThat(dto.getId()).isEqualTo(99L);
        assertThat(dto.getConversationId()).isEqualTo(convId);
        assertThat(dto.getSenderId()).isEqualTo(senderId);
        assertThat(dto.getRecipientId()).isEqualTo(recipientId);
        assertThat(dto.getContent()).isEqualTo("Hola!");
        assertThat(dto.getAttachmentUrl()).isEqualTo("http://img.jpg");
        assertThat(dto.getStatus()).isEqualTo(MessageStatus.SENT);
        assertThat(dto.getSentAt()).isEqualTo(now);
    }
}
