// --- src/main/java/alatoo/edu/edtechmentorshipplatform/entity/SessionRecording.java ---
package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.AccessLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_recordings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionRecording {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private MentorshipSession session;

    @Column(nullable = false, length = 1000)
    private String recordingUrl;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessLevel accessLevel;
}
