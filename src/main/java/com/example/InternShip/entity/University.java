package com.example.InternShip.entity;

import jakarta.persistence.*;

public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "university")
    private Intern intern;
}
