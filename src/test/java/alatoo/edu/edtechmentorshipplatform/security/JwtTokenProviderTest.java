package alatoo.edu.edtechmentorshipplatform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider provider;
    private String secret;
    private long expirationMillis;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        provider = new JwtTokenProvider();
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        secret = Base64.getEncoder().encodeToString(key.getEncoded());
        expirationMillis = 1000L;

        ReflectionTestUtils.setField(provider, "secretKey", secret);
        ReflectionTestUtils.setField(provider, "jwtExpiration", expirationMillis);
        ReflectionTestUtils.setField(provider, "jwtRefreshExpiration", expirationMillis * 2);

        userDetails = User.withUsername("testuser").password("pwd").authorities("ROLE_USER").build();
    }

    @Test
    void generateAndExtractUsername() {
        String token = provider.generateToken(userDetails);
        assertThat(provider.extractUsername(token)).isEqualTo("testuser");
    }

    @Test
    void tokenIsValidBeforeExpiration() {
        String token = provider.generateToken(userDetails);
        assertThat(provider.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void tokenIsInvalidAfterExpiration_throwsExpiredJwtException() throws InterruptedException {
        String token = provider.generateToken(userDetails);
        Thread.sleep(expirationMillis + 10);
        assertThatThrownBy(() -> provider.isTokenValid(token, userDetails))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void refreshTokenHasLongerExpiration_allowsRefreshAfterAccessExpired() throws InterruptedException {
        String token = provider.generateToken(userDetails);
        String refresh = provider.generateRefreshToken(userDetails);
        Thread.sleep(expirationMillis + 10);

        // access token expired
        assertThatThrownBy(() -> provider.isTokenValid(token, userDetails))
                .isInstanceOf(ExpiredJwtException.class);

        // refresh token still valid
        assertThat(provider.isTokenValid(refresh, userDetails)).isTrue();
    }

    @Test
    void extractClaim_viaFunction() {
        String token = provider.generateToken(userDetails);
        Date issued = provider.extractClaim(token, Claims::getIssuedAt);
        assertThat(issued).isBeforeOrEqualTo(new Date());
    }

    @Test
    void extractAllClaimsWithWrongSecret_throws() {
        String token = provider.generateToken(userDetails);
        Key newKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        ReflectionTestUtils.setField(provider, "secretKey", Base64.getEncoder().encodeToString(newKey.getEncoded()));
        assertThatThrownBy(() -> provider.extractUsername(token))
                .isInstanceOf(Exception.class);
    }
}
