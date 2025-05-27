package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.entity.RefreshToken;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.exception.RefreshTokenException;
import alatoo.edu.edtechmentorshipplatform.repo.RefreshTokenRepo;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.security.JwtTokenProvider;
import alatoo.edu.edtechmentorshipplatform.services.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepo refreshTokenRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtTokenProvider jwtProvider;

    @InjectMocks
    private RefreshTokenServiceImpl service;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                   .id(userId)
                   .email("u@example.com")
                   .password("pass")
                   .build();

        ReflectionTestUtils.setField(service, "refreshDurationMs", 1_000L);
    }

    @Test
    void createRefreshToken_whenUserNotFound_throwsNotFound() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createRefreshToken(userId))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("User not found");
    }

    @Test
    void createRefreshToken_whenNoExisting_savesNewToken() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(jwtProvider.generateRefreshToken(any())).thenReturn("new-token");
        when(refreshTokenRepo.findRefreshTokenByUserId(userId))
            .thenReturn(Optional.empty());

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        RefreshToken stubSaved = RefreshToken.builder()
                                             .id(5L)
                                             .user(user)
                                             .token("new-token")
                                             .expiresAt(Instant.now().plusMillis(1_000L))
                                             .build();
        when(refreshTokenRepo.save(any())).thenReturn(stubSaved);

        RefreshToken result = service.createRefreshToken(userId);

        verify(refreshTokenRepo).save(captor.capture());
        RefreshToken toSave = captor.getValue();
        assertThat(toSave.getUser()).isSameAs(user);
        assertThat(toSave.getToken()).isEqualTo("new-token");
        assertThat(toSave.getExpiresAt()).isAfter(Instant.now());

        assertThat(result).isSameAs(stubSaved);
    }

    @Test
    void createRefreshToken_whenExisting_updatesTokenAndExpiry() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(jwtProvider.generateRefreshToken(any())).thenReturn("updated-token");

        RefreshToken existing = RefreshToken.builder()
                                            .id(7L)
                                            .user(user)
                                            .token("old")
                                            .expiresAt(Instant.now())
                                            .build();
        when(refreshTokenRepo.findRefreshTokenByUserId(userId))
            .thenReturn(Optional.of(existing));

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        RefreshToken saved = RefreshToken.builder()
                                         .id(7L)
                                         .user(user)
                                         .token("updated-token")
                                         .expiresAt(Instant.now().plusMillis(1_000L))
                                         .build();
        when(refreshTokenRepo.save(existing)).thenReturn(saved);

        RefreshToken result = service.createRefreshToken(userId);

        verify(refreshTokenRepo).save(existing);
        assertThat(existing.getToken()).isEqualTo("updated-token");
        assertThat(existing.getExpiresAt()).isAfter(Instant.now());

        assertThat(result).isSameAs(saved);
    }

    @Test
    void verifyExpiration_whenNotExpired_returnsSameToken() {
        RefreshToken t = RefreshToken.builder()
                                     .expiresAt(Instant.now().plusSeconds(5))
                                     .build();

        RefreshToken out = service.verifyExpiration(t);
        assertThat(out).isSameAs(t);
    }

    @Test
    void verifyExpiration_whenExpired_deletesAndThrows() {
        RefreshToken expired = RefreshToken.builder()
                                           .expiresAt(Instant.now().minusSeconds(1))
                                           .build();

        assertThatThrownBy(() -> service.verifyExpiration(expired))
            .isInstanceOf(RefreshTokenException.class)
            .hasMessage("Refresh token expired. Please login again.");

        verify(refreshTokenRepo).delete(expired);
    }

    @Test
    void findByToken_delegatesToRepo() {
        RefreshToken t = new RefreshToken();
        when(refreshTokenRepo.findByToken("abc")).thenReturn(Optional.of(t));

        Optional<RefreshToken> out = service.findByToken("abc");
        assertThat(out).containsSame(t);
        verify(refreshTokenRepo).findByToken("abc");
    }

    @Test
    void deleteCurrentRefreshTokenByUserId_whenExists_deletes() {
        RefreshToken t = new RefreshToken();
        when(refreshTokenRepo.findRefreshTokenByUserId(userId)).thenReturn(Optional.of(t));

        service.deleteCurrentRefreshTokenByUserId(userId);

        verify(refreshTokenRepo).delete(t);
    }

    @Test
    void deleteCurrentRefreshTokenByUserId_whenNotExists_noInteraction() {
        when(refreshTokenRepo.findRefreshTokenByUserId(userId)).thenReturn(Optional.empty());

        service.deleteCurrentRefreshTokenByUserId(userId);

        verify(refreshTokenRepo, never()).delete(any());
    }
}
