package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.entity.RefreshToken;
import alatoo.edu.edtechmentorshipplatform.exception.RefreshTokenException;
import alatoo.edu.edtechmentorshipplatform.security.JwtTokenProvider;
import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginResponse;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterResponse;
import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Role;
import alatoo.edu.edtechmentorshipplatform.exception.EmailAlreadyExistsException;
import alatoo.edu.edtechmentorshipplatform.exception.ValidationException;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import alatoo.edu.edtechmentorshipplatform.security.UserDetailsImpl;
import alatoo.edu.edtechmentorshipplatform.services.RefreshTokenService;
import alatoo.edu.edtechmentorshipplatform.services.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() == null ? Role.MENTEE : request.getRole())
                .isActive(true)
                .isEmailVerified(false)
                .build();

        userRepository.save(user);

        UserDetails userDetails = new UserDetailsImpl(user);
        String token = jwtTokenProvider.generateToken(userDetails);

        return RegisterResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            throw new ValidationException("Invalid email or password");
        }

        UserDetails userDetails = new UserDetailsImpl(userOpt.get());
        String token = jwtTokenProvider.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userOpt.get().getId()).getToken();

        return LoginResponse.builder()
                .userId(userOpt.get().getId())
                .email(userOpt.get().getEmail())
                .role(userOpt.get().getRole())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginResponse refreshTokens(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));
        refreshTokenService.verifyExpiration(tokenEntity);

        var user = tokenEntity.getUser();
        var userDetails = new UserDetailsImpl(user);
        String newAccessToken = jwtTokenProvider.generateToken(userDetails);
        String newRefreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();

        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


    @Override
    public void logoutCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            UUID userId = ((UserDetailsImpl) auth.getPrincipal()).getUser().getId();
            refreshTokenService.deleteCurrentRefreshTokenByUserId(userId);
        }
    }
}
