package alatoo.edu.edtechmentorshipplatform.repo;

import alatoo.edu.edtechmentorshipplatform.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findRefreshTokenByUserId(UUID userId);
}