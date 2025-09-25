package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class InternshipPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String namePosition;

    private int quantity;

    private String description;

    @OneToMany(mappedBy = "internshipPosition")
    private List<Intern> intern;

    @ManyToOne
    @JoinColumn(name = "internship_program_id", nullable = false)
    private InternshipProgram internshipProgram;
}
