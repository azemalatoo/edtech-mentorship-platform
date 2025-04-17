package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.CertificateRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certificate_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", referencedColumnName = "id", nullable = false)
    private User mentee;

    @ManyToOne
    @JoinColumn(name = "goal_id", referencedColumnName = "id")
    private LearningGoal goal;

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private MentorshipSession session;

    private LocalDateTime requestedAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private CertificateRequestStatus status;

    private UUID approvedBy;
    private LocalDateTime approvedAt;
    private String certificateUrl;
}
