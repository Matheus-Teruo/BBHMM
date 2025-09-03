package com.BBHMM.backend.BBHMM.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.BBHMM.backend.BBHMM.models.request.UserUpdateRequest;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullname;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pix pix;

    @Column()

    @ManyToMany(mappedBy = "users")
    private Set<Event> events = new HashSet<>();

    public User(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    public void updateUser(UserUpdateRequest request, String password, boolean passwordFlag) {
        if (request.username() != null) {
            this.username = request.username();
        }
        if (passwordFlag) {
            this.password = password;
        }
        if (request.fullname() != null) {
            this.fullname = request.fullname();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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
