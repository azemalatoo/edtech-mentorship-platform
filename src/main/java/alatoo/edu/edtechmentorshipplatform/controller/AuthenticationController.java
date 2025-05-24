package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.controller.base.BaseRestController;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthenticationController", description = "APIs for user authentication")
@RequiredArgsConstructor
public class AuthenticationController extends BaseRestController{

    private final UserAuthService authService;

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user input")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<RegisterResponse> register(
            @Parameter(description = "User registration request details")
            @Valid @RequestBody RegisterRequest request) {
        RegisterResponse registeredUser = authService.register(request);
        return new ResponseApi<>(registeredUser, ResponseCode.CREATED);
    }

    @Operation(summary = "Login with credentials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, returns token"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<LoginResponse> login(
            @Parameter(description = "User login request with username and password")
            @Valid @RequestBody LoginRequest request) {
        LoginResponse token = authService.login(request);
        return new ResponseApi<>(token, ResponseCode.SUCCESS);
    }
}
