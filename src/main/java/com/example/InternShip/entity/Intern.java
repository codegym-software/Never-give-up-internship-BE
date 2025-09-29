package com.example.InternShip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Intern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private University university;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Getter
    @RequiredArgsConstructor
    public enum Status{
        ACTIVE,
        SUSPENDED,
        COMPLETED,
        DROPPED;
    }
}
