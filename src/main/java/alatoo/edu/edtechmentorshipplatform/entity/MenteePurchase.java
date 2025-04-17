package alatoo.edu.edtechmentorshipplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mentee_purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenteePurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mentee_id", referencedColumnName = "id", nullable = false)
    private User mentee;

    @ManyToOne
    @JoinColumn(name = "package_id", referencedColumnName = "id", nullable = false)
    private TutoringPackage tutoringPackage;

    private LocalDateTime purchasedAt = LocalDateTime.now();
    private LocalDateTime expiresAt;
    private int sessionsRemaining;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}
