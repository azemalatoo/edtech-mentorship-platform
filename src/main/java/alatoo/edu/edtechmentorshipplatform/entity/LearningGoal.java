package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.GoalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    @Column(nullable = false)
    private String goalTitle;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Boolean isAchieved = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime achievedAt;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @Column(length = 2000)
    private String feedback;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GoalStatus status = GoalStatus.PENDING;

    private LocalDate targetDate;
    private Integer progressPercentage = 0;
    @Column(length = 2000)
    private String progressNotes;
}