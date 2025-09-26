package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash")
    private String password; // Giữ nguyên tên trường của bạn

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column()
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    private String address;

    @ManyToOne
    @JoinColumn(name = "role_Id") // Giữ nguyên tên cột của bạn
    private Role role;

    @OneToOne(mappedBy = "user")
    private Mentor mentor;

    @OneToOne(mappedBy = "user")
    private Intern intern;

    @OneToMany(mappedBy = "user")
    private List<InternshipApplication> internshipApplication;

    @OneToOne(mappedBy = "user")
    private UserOauth userOauth;

    @Column(name = "isActive")
    private boolean isActive = true; // Giữ nguyên tên trường của bạn

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
    }

    @Override
    public String getPassword() {
        return this.password; // Trả về trường 'password' gốc của bạn
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive; // Trả về trường 'isActive' gốc của bạn
    }
}