package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.entity.RefreshToken;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.exception.RefreshTokenException;
import alatoo.edu.edtechmentorshipplatform.repo.RefreshTokenRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.security.JwtTokenProvider;
import alatoo.edu.edtechmentorshipplatform.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;
    private final JwtTokenProvider jwtProvider;

    @Value("${jwt.refresh-expiration}")
    private long refreshDurationMs;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String newTokenValue = jwtProvider.generateRefreshToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), List.of())
        );
        Instant newExpiry = Instant.now().plusMillis(refreshDurationMs);

        Optional<RefreshToken> existingOpt = refreshTokenRepo.findRefreshTokenByUserId(userId);

        if (existingOpt.isPresent()) {
            RefreshToken existing = existingOpt.get();
            existing.setToken(newTokenValue);
            existing.setExpiresAt(newExpiry);
            return refreshTokenRepo.save(existing);
        } else {
            RefreshToken token = RefreshToken.builder()
                    .user(user)
                    .token(newTokenValue)
                    .expiresAt(newExpiry)
                    .build();
            return refreshTokenRepo.save(token);
        }
    }


    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepo.delete(token);
            throw new RefreshTokenException("Refresh token expired. Please login again.");
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public void deleteCurrentRefreshTokenByUserId(UUID userId) {
        Optional<RefreshToken> refreshToken = refreshTokenRepo.findRefreshTokenByUserId(userId);
        if(refreshToken.isPresent()) {
            refreshTokenRepo.delete(refreshToken.get());
        }
    }
}
