package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "intern_id", nullable = false)
    private Intern intern;

    private LocalDate date;

    private RequestType requestType;

    private String reason;

    private Boolean isApproved = null;

    @Getter
    public enum RequestType {
        LEAVE_FULL_DAY("Nghỉ phép cả ngày"),
        LEAVE_HALF_DAY_MORNING("Nghỉ phép nửa ngày buổi sáng"),
        LEAVE_HALF_DAY_AFTERNOON("Nghỉ phép nửa ngày buổi chiều"),
        LATE("Xin phép đến muộn"),
        EARLY("Xin về sớm");

        private final String label;

        RequestType(String label) {
            this.label = label;
        }
    }
}
