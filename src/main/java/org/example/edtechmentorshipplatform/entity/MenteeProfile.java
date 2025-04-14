package org.example.edtechmentorshipplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.persistence.*;
import org.example.edtechmentorshipplatform.enums.Lang;

@Entity
@Table(name = "mentee_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenteeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String educationLevel;
    private String fieldOfStudy;
    private String careerGoal;

    private Lang preferredLanguage;
}
