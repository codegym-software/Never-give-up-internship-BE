package com.example.InternShip.entity;

import com.example.InternShip.entity.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "intern_id")
    private Intern intern;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}
