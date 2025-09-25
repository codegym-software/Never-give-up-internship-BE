package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "internship_term_id"})
        }
)
public class InternshipApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String cvUrl;

    @OneToMany(mappedBy = "internshipApplication")
    private List<Certificate> certificate;

    private Status status = Status.PENDING;

    private int score;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne
    @JoinColumn(name = "internship_program_id", nullable = false)
    private InternshipProgram internshipProgram;

    @Getter
    public enum Status{
        PENDING("Chờ duyệt"),       // Mới nộp, chưa được xét duyệt
        APPROVED("Đã duyệt"),       // Đơn được duyệt, chuẩn bị cho thực tập
        REJECTED("Từ chối"),        // Đơn bị từ chối
        WITHDRAWN("Rút đơn");       // Người nộp rút lại đơn

        private final String label;

        Status(String label) {
            this.label = label;
        }
    }
}
