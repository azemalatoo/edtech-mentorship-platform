package alatoo.edu.edtechmentorshipplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiresAt;
}
