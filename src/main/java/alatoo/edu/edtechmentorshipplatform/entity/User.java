package alatoo.edu.edtechmentorshipplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import alatoo.edu.edtechmentorshipplatform.enums.Role;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String avatarUrl;
    private String languagePref;
    private String phoneNumber;
    private String timezone;
    private String location;

    @Column(nullable = false)
    private Boolean isEmailVerified = false;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsActive(){
        return isActive;
    }
}
