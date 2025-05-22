package alatoo.edu.edtechmentorshipplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import alatoo.edu.edtechmentorshipplatform.enums.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id", nullable = false)
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id", referencedColumnName = "id", nullable = false)
    private User mentee;

    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(length = 1000)
    private String notes;

    private boolean isCompleted = false;

    @ManyToOne
    @JoinColumn(name = "package_id", referencedColumnName = "id")
    private TutoringPackage tutoringPackage;
}
