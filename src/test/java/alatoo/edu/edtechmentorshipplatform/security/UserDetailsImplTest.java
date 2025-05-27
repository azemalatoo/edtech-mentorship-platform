package alatoo.edu.edtechmentorshipplatform.security;

import alatoo.edu.edtechmentorshipplatform.entity.User;
import alatoo.edu.edtechmentorshipplatform.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    @Test
    void getAuthorities_shouldReturnRoleAuthority() {
        // given
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("user@example.com")
                .password("secret")
                .role(Role.MENTOR)
                .isActive(true)
                .build();

        UserDetailsImpl details = new UserDetailsImpl(user);

        // when
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        // then: exactly one authority with expected role prefix
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority())
                .isEqualTo("ROLE_MENTOR");
    }

    @Test
    void getPasswordAndUsername_shouldReturnUserFields() {
        // given
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("alice@example.com")
                .password("pass123")
                .role(Role.MENTEE)
                .isActive(true)
                .build();
        UserDetailsImpl details = new UserDetailsImpl(user);

        // when / then
        assertThat(details.getUsername()).isEqualTo("alice@example.com");
        assertThat(details.getPassword()).isEqualTo("pass123");
    }

    @Test
    void accountStatusFlags_shouldAlwaysBeTrue() {
        // given
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("bob@example.com")
                .password("pwd")
                .role(Role.ADMIN)
                .isActive(true)
                .build();
        UserDetailsImpl details = new UserDetailsImpl(user);

        // when / then
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void isEnabled_shouldReflectUserIsActive() {
        // active user
        User activeUser = User.builder()
                .id(UUID.randomUUID())
                .email("active@example.com")
                .password("pwd")
                .role(Role.MENTEE)
                .isActive(true)
                .build();
        UserDetailsImpl activeDetails = new UserDetailsImpl(activeUser);
        assertThat(activeDetails.isEnabled()).isTrue();

        // inactive user
        User inactiveUser = User.builder()
                .id(UUID.randomUUID())
                .email("inactive@example.com")
                .password("pwd")
                .role(Role.MENTEE)
                .isActive(false)
                .build();
        UserDetailsImpl inactiveDetails = new UserDetailsImpl(inactiveUser);
        assertThat(inactiveDetails.isEnabled()).isFalse();
    }

    @Test
    void getUser_shouldReturnWrappedUser() {
        // given
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("pwd")
                .role(Role.MENTEE)
                .isActive(true)
                .build();
        UserDetailsImpl details = new UserDetailsImpl(user);

        // when / then
        assertThat(details.getUser()).isSameAs(user);
    }
}
