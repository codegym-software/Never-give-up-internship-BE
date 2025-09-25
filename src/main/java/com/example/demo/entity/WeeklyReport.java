package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class WeeklyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "intern_id", nullable = false)
    private Intern intern;

    private String content;

    private LocalDateTime submittedAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean isRead = false;

    @OneToMany(mappedBy = "weeklyReport")
    private Set<MentorFeedback> mentorFeedbackList;

}
