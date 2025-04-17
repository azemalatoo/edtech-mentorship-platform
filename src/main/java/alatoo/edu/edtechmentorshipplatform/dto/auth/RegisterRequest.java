package alatoo.edu.edtechmentorshipplatform.dto.auth;

import alatoo.edu.edtechmentorshipplatform.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private Role role;
}