package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginResponse;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterResponse;
import alatoo.edu.edtechmentorshipplatform.entity.RefreshToken;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Role;
import alatoo.edu.edtechmentorshipplatform.exception.EmailAlreadyExistsException;
import alatoo.edu.edtechmentorshipplatform.exception.RefreshTokenException;
import alatoo.edu.edtechmentorshipplatform.exception.ValidationException;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.security.UserDetailsImpl;
import alatoo.edu.edtechmentorshipplatform.security.JwtTokenProvider;
import alatoo.edu.edtechmentorshipplatform.services.impl.UserAuthServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock private UserRepo userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private RefreshTokenService refreshTokenService;

    @InjectMocks
    private UserAuthServiceImpl authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .password("encodedPass")
                .role(Role.MENTEE)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_WithNewEmail_ReturnsRegisterResponse() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("new@example.com");
        req.setPassword("plain");
        req.setFullName("New User");
        req.setRole(Role.MENTOR);

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plain")).thenReturn("enc");
        UUID assignedId = UUID.randomUUID();
        // capture the saved user and assign an ID
        when(userRepository.save(any(User.class)))
            .thenAnswer(inv -> {
                User u = inv.getArgument(0);
                u.setId(assignedId);
                return u;
            });
        when(jwtTokenProvider.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        RegisterResponse resp = authService.register(req);

        assertThat(resp.getUserId()).isEqualTo(assignedId);
        assertThat(resp.getEmail()).isEqualTo("new@example.com");
        assertThat(resp.getRole()).isEqualTo(Role.MENTOR);
        assertThat(resp.getToken()).isEqualTo("jwt-token");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("new@example.com");
        assertThat(saved.getFullName()).isEqualTo("New User");
        assertThat(saved.getPassword()).isEqualTo("enc");
        assertThat(saved.getRole()).isEqualTo(Role.MENTOR);
        assertThat(saved.getIsActive()).isTrue();
        assertThat(saved.getIsEmailVerified()).isFalse();
    }

    @Test
    void register_WithExistingEmail_ThrowsEmailAlreadyExistsException() {
        when(userRepository.existsByEmail("dup@example.com")).thenReturn(true);
        RegisterRequest req = new RegisterRequest();
        req.setEmail("dup@example.com");
        req.setPassword("x");

        assertThatThrownBy(() -> authService.register(req))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessage("Email already exists.");
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_WithValidCredentials_ReturnsLoginResponse() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("plain");

        when(userRepository.findByEmail("user@example.com"))
            .thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("plain", "encodedPass")).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(UserDetails.class))).thenReturn("access-jwt");
        when(refreshTokenService.createRefreshToken(sampleUser.getId()))
            .thenReturn(RefreshToken.builder().token("refresh-val").build());

        LoginResponse resp = authService.login(req);

        assertThat(resp.getUserId()).isEqualTo(sampleUser.getId());
        assertThat(resp.getEmail()).isEqualTo("user@example.com");
        assertThat(resp.getRole()).isEqualTo(Role.MENTEE);
        assertThat(resp.getAccessToken()).isEqualTo("access-jwt");
        assertThat(resp.getRefreshToken()).isEqualTo("refresh-val");
    }

    @Test
    void login_WithUnknownEmail_ThrowsValidationException() {
        when(userRepository.findByEmail("bad@x.com")).thenReturn(Optional.empty());
        LoginRequest req = new LoginRequest();
        req.setEmail("bad@x.com");
        req.setPassword("p");

        assertThatThrownBy(() -> authService.login(req))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Invalid email or password");
    }

    @Test
    void login_WithWrongPassword_ThrowsValidationException() {
        when(userRepository.findByEmail("user@example.com"))
            .thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("nop", "encodedPass")).thenReturn(false);
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("nop");

        assertThatThrownBy(() -> authService.login(req))
            .isInstanceOf(ValidationException.class)
            .hasMessage("Invalid email or password");
    }

    @Test
    void refreshTokens_WithValidToken_ReturnsNewTokens() {
        RefreshToken old = RefreshToken.builder()
                .user(sampleUser)
                .expiresAt(Instant.now().plusSeconds(60))
                .build();
        when(refreshTokenService.findByToken("old")).thenReturn(Optional.of(old));
        when(refreshTokenService.verifyExpiration(old)).thenReturn(old);
        when(jwtTokenProvider.generateToken(any(UserDetails.class))).thenReturn("new-access");
        when(refreshTokenService.createRefreshToken(sampleUser.getId()))
            .thenReturn(RefreshToken.builder().token("new-refresh").build());

        LoginResponse resp = authService.refreshTokens("old");

        assertThat(resp.getAccessToken()).isEqualTo("new-access");
        assertThat(resp.getRefreshToken()).isEqualTo("new-refresh");
        assertThat(resp.getUserId()).isEqualTo(sampleUser.getId());
        assertThat(resp.getEmail()).isEqualTo("user@example.com");
        assertThat(resp.getRole()).isEqualTo(Role.MENTEE);
    }

    @Test
    void refreshTokens_WithInvalidToken_ThrowsRefreshTokenException() {
        when(refreshTokenService.findByToken("bad")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.refreshTokens("bad"))
            .isInstanceOf(RefreshTokenException.class)
            .hasMessage("Invalid refresh token");
    }

    @Test
    void logoutCurrentUser_WhenAuthenticated_DeletesRefreshToken() {
        UserDetailsImpl ud = new UserDetailsImpl(sampleUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(ud, null);
        SecurityContext ctx = new SecurityContextImpl();
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);

        authService.logoutCurrentUser();

        verify(refreshTokenService).deleteCurrentRefreshTokenByUserId(sampleUser.getId());
    }

    @Test
    void logoutCurrentUser_WhenNoAuthentication_DoesNothing() {
        SecurityContextHolder.clearContext();
        authService.logoutCurrentUser();
        verifyNoInteractions(refreshTokenService);
    }
}
