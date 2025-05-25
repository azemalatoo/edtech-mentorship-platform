package alatoo.edu.edtechmentorshipplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import alatoo.edu.edtechmentorshipplatform.enums.SessionProviderType;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "mentorship_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorshipSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private User mentee;

    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Enumerated(EnumType.STRING)
    private SessionProviderType providerType;

    private String meetingLink;

    @Column(length = 1000)
    private String notes;
}