package com.example.InternShip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@Table(name = "internship_program")
public class InternshipProgram {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private LocalDate timeStart;
    private LocalDate timeEnd;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "internshipProgram")
    @JsonIgnore
    private List<InternshipApplication> applications;

    public enum Status {
        DRAFT, // bản nháp
        PUBLISHED, // xuất bản
        REVIEWING, // Đang trong quá trình xem xét
        ONGOING, // đang thực hiện
        COMPLETED, // hoàn thành
        CANCELLED // huỷ bỏ
    }
}

