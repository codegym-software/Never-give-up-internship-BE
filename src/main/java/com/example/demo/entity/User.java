package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column()
    private String phone;

    @Column(name = "avatar")
    private String avatar;

    private String address;

    @ManyToOne
    @JoinColumn(name = "role_Id")
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
    private boolean isActive = true;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

}
