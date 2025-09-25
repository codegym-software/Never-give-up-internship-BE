package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "intern_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    private String Project;

    @ManyToOne
    @JoinColumn(name = "internship_program_id", nullable = false)
    private InternshipProgram internshipProgram;

    @OneToMany(mappedBy = "group")
    private List<Intern> intern;

    @OneToMany(mappedBy = "group")
    private List<WorkSchedule> workSchedules;
}
