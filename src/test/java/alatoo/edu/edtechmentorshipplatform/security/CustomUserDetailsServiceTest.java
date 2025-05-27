package alatoo.edu.edtechmentorshipplatform.security;

import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Role;
import alatoo.edu.edtechmentorshipplatform.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CustomUserDetailsService service;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .email("test@example.com")
                .password("encryptedPass")
                .role(Role.MENTEE)
                .build();
    }

    @Test
    void loadUserByUsername_existingUser_returnsUserDetails() {
        // given
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // when
        UserDetails details = service.loadUserByUsername("test@example.com");

        // then
        assertThat(details).isInstanceOf(UserDetailsImpl.class);
        assertThat(details.getUsername()).isEqualTo("test@example.com");
        assertThat(details.getPassword()).isEqualTo("encryptedPass");
        assertThat(details.getAuthorities())
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_MENTEE"));
    }

    @Test
    void loadUserByUsername_nonExistingUser_throwsException() {
        // given
        when(userRepo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown@example.com"));
    }
}