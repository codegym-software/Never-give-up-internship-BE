package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"intern_id", "date"})
        }
)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "intern_id", nullable = false)
    private Intern intern;

    private LocalDate date = LocalDate.now();

    private LocalTime checkIn;

    private LocalTime checkOut;

    private Status status;

    @Getter
    public enum Status{
        WORK_FULL("Đi làm cả ngày", "x"),
        WORK_HALF("Đi làm nửa ngày", "x/2"),
        ABSENT_PAID_FULL("Nghỉ phép cả ngày", "P"),
        ABSENT_PAID_HALF("Nghỉ phép nửa ngày", "P/2"),
        ABSENT_UNPAID_FULL("Nghỉ cả ngày không phép", "Ro"),
        ABSENT_UNPAID_HALF("Nghỉ nửa ngày không phép", "Ro/2"),
        WORK_HALF_ABSENT_PAID_HALF("Đi làm nửa ngày + nghỉ nửa ngày có phép", "x/2 + P/2"),
        WORK_HALF_ABSENT_UNPAID_HALF("Đi làm nửa ngày + nghỉ nửa ngày không phép", "x/2 + Ro/2"),
        LATE_YES("Xin phép đến muộn", "xM"),
        LATE_NO("Không xin phép đến muộn", "xMRo"),
        EARLY_YES("Xin về sớm", "xVe"),
        EARLY_NO("Không xin phép về sớm", "xVeRo");

        private final String label;
        private final String symbol;

        Status(String label, String symbol) {
            this.label = label;
            this.symbol = symbol;
        }
    }
}
