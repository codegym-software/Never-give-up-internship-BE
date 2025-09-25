package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Column(unique = true, nullable = false)
    private String certificateUrl;

    @ManyToOne
    @JoinColumn(name = "internship_application_id", nullable = false)
    private InternshipApplication internshipApplication;
}
