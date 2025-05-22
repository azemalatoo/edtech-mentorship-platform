package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.AccessLevel;
import alatoo.edu.edtechmentorshipplatform.enums.StorageProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id", nullable = false)
    private MentorshipSession session;

    @Column(nullable = false)
    private String recordingUrl;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StorageProvider storageProvider;

    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

}
