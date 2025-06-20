package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "mentee_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenteeProfile {
    @Id
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String educationLevel;

    private String fieldOfStudy;

    private String careerGoal;

    @Enumerated(EnumType.STRING)
    private Lang preferredLanguage;
}
