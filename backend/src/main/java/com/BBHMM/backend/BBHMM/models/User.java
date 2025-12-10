package com.BBHMM.backend.BBHMM.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.BBHMM.backend.BBHMM.models.request.CreateGuestRequest;
import com.BBHMM.backend.BBHMM.models.request.SignupUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpgradeGuestToUserRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateUserRequest;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(nullable = false, length = 100)
    private String fullname;

    @Setter
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pix pix;

    @Column(nullable = false, length = 255)
    private String email;

    @ManyToMany(mappedBy = "users")
    private Set<Event> events = new HashSet<>();

    public User(SignupUserRequest request, String password) {
        this.username = request.username();
        this.password = password;
        this.role = RoleEnum.ROLE_USER;
        this.emailVerified = false;
        this.fullname = request.fullname();
        this.email = request.email();
    }

    public User(CreateGuestRequest request, String password) {
        this.username = request.guestName();
        this.password = password;
        this.role = RoleEnum.ROLE_GUEST;
        this.emailVerified = false;
        this.fullname = request.guestName();
    }

    public void updateUser(UpdateUserRequest request) {
        if (request.username() != null) {
            this.username = request.username();
        }
        if (request.fullname() != null) {
            this.fullname = request.fullname();
        }
        if (request.email() != null) {
            this.email = request.email();
        }
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void upgradeGuestToUser(UpgradeGuestToUserRequest request, String password) {
        this.password = password;
        this.email = request.email();
        this.role = RoleEnum.ROLE_USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
