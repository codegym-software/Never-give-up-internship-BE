package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class InternshipProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate timeStart;

    private LocalDate timeEnd;

    private Status status;

    @OneToMany(mappedBy = "internshipProgram")
    private List<Group> group;

    @OneToMany(mappedBy = "internshipProgram")
    private List<InternshipPosition> internshipPosition;

    @OneToMany(mappedBy = "internshipProgram")
    private List<InternshipApplication> internshipApplication;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Getter
    public enum Status{
        UPCOMING("Sắp diễn ra"),      // Chưa bắt đầu
        ONGOING("Đang diễn ra"),       // Đang thực hiện
        COMPLETED("Đã kết thúc"),      // Hoàn tất
        CANCELLED("Đã hủy");           // Bị hủy

        private final String label;

        Status(String label) {
            this.label = label;
        }

    }
}
