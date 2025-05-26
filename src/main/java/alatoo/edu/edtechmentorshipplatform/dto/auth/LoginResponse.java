package alatoo.edu.edtechmentorshipplatform.dto.auth;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import alatoo.edu.edtechmentorshipplatform.enums.Role;

@Data
@Builder
public class LoginResponse {
    private UUID userId;
    private String email;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
