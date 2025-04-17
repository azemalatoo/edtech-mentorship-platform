package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "learning_goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", referencedColumnName = "id", nullable = false)
    private User mentee;

    private String goalTitle;
    private String description;

    private Boolean isAchieved;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime achievedAt;

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id", nullable = true)
    private User mentor;

    private String feedback;

    @Column(nullable = false)
    private GoalStatus status;
}
