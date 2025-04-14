package org.example.edtechmentorshipplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "tutoring_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutoringPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id", nullable = false)
    private User mentor;

    private String title;
    private String description;
    private BigDecimal price;
    private int durationDays;
    private int sessionLimit;
    private boolean supportIncluded;
    private boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
