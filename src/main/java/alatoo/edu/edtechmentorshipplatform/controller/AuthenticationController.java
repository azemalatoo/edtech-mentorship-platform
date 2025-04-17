package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginResponse;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterResponse;
import alatoo.edu.edtechmentorshipplatform.services.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserAuthService authService;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user input")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Parameter(description = "User registration request details")
            @RequestBody RegisterRequest request) {
        RegisterResponse registeredUser = authService.register(request);
        return ResponseEntity.ok(registeredUser);
    }

    @Operation(summary = "Login with credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, returns token"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "User login request with username and password")
            @RequestBody LoginRequest request) {
        LoginResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}
