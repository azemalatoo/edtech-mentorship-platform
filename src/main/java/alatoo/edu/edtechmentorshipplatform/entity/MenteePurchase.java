package alatoo.edu.edtechmentorshipplatform.entity;

import alatoo.edu.edtechmentorshipplatform.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentee_purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenteePurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
