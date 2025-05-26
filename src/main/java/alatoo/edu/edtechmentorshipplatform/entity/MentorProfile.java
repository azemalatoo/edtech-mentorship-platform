package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.Lang;
import alatoo.edu.edtechmentorshipplatform.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mentor_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String bio;
    private String headline;
    private int yearsExperience;
    private String linkedinUrl;

    @ElementCollection
    private List<String> certifications = new ArrayList<>();

    @ElementCollection
    private List<Lang> languages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "expertise_category_id", referencedColumnName = "id")
    private Category expertiseCategory;

    private double averageRating;

    private ProfileStatus profileStatus;
}
