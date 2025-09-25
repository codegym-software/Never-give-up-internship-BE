package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Intern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String position;

    @ManyToOne
    @JoinColumn(name = "internship_position_id", nullable = false)
    private InternshipPosition internshipPosition;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "intern")
    private List<Attendance> attendance;

    @OneToMany(mappedBy = "intern")
    private List<Request> request;

    @OneToMany(mappedBy = "intern")
    private List<Allowance> allowances;

    @OneToMany(mappedBy = "intern")
    private List<WeeklyReport> weeklyReports;

    @OneToMany(mappedBy = "intern")
    private List<Evaluation> evaluation;

    @OneToMany(mappedBy = "intern")
    private List<SupportRequest> supportRequests;

    @ManyToMany
    @JoinTable(name = "intern_task",
            joinColumns = @JoinColumn(name = "intern_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private Set<Task> task;

    private LocalDate startDate;

    private LocalDate endDate;

    private Status status;

    @Getter
    public enum Status{
        ACTIVE("Đang thực tập"),
        SUSPENDED("Tạm ngưng"),
        COMPLETED("Hoàn thành"),
        DROPPED("Bỏ thực tập");

        private final String label;

        Status(String label) {
            this.label = label;
        }
    }
}
