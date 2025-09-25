package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String schoolName;

    private String Location;

    @OneToMany(mappedBy = "school")
    private List<InternshipApplication> internshipApplication;

    @OneToMany(mappedBy = "school")
    private List<Intern> interns;
}
