package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginResponse;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterResponse;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import alatoo.edu.edtechmentorshipplatform.services.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthenticationController", description = "APIs for user authentication")
@RequiredArgsConstructor
public class AuthenticationController{

    private final UserAuthService authService;

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user input")
    })
    @PostMapping("/register")
    public ResponseApi<RegisterResponse> register(
            @Parameter(description = "User registration request details")
            @Valid @RequestBody RegisterRequest request) {
        RegisterResponse registeredUser = authService.register(request);
        return new ResponseApi<>(registeredUser, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Login with credentials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, returns token"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseApi<LoginResponse> login(
            @Parameter(description = "User login request with username and password")
            @Valid @RequestBody LoginRequest request) {
        LoginResponse token = authService.login(request);
        return new ResponseApi<>(token, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Refresh access token", description = "Exchange a refresh token for a new access (and refresh) token pair")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Refresh token expired or unauthorized")
    })
    @PostMapping("/refresh")
    public ResponseApi<LoginResponse> refresh(
            @RequestParam("refreshToken") String refreshToken) {
        LoginResponse resp = authService.refreshTokens(refreshToken);
        return new ResponseApi<>(resp, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Logout user", description = "Revoke current user's refresh token and end session")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseApi<Void> logout() {
        authService.logoutCurrentUser();
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
