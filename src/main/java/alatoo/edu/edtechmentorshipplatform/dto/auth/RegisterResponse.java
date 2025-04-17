package alatoo.edu.edtechmentorshipplatform.dto.auth;

import alatoo.edu.edtechmentorshipplatform.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RegisterResponse {
    private UUID userId;
    private String email;
    private Role role;
    private String token;
}
