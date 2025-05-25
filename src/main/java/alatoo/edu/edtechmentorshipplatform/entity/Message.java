package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(length = 2000)
    private String content;

    private String attachmentUrl;

    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private boolean isImportant;

    private String messageType;
}