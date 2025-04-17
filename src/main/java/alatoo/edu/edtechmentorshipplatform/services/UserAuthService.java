package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginRequest;
import alatoo.edu.edtechmentorshipplatform.dto.auth.LoginResponse;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterResponse;
import alatoo.edu.edtechmentorshipplatform.dto.auth.RegisterRequest;

public interface UserAuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
