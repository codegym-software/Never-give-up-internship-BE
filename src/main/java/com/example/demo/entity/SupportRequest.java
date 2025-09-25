package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class SupportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "intern_id", nullable = false)
    private Intern intern;

    private Type type;

    private String description;

    private Status status;

    private String feedback;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    @Getter
    public enum Type{
        DOCUMENT("Hỗ trợ giấy tờ / chứng nhận"),
        ACCOUNT("Hỗ trợ tài khoản / đăng nhập"),
        EQUIPMENT("Hỗ trợ thiết bị / phần cứng"),
        OTHER("Hỗ trợ khác");

        private final String label;

        Type(String label) {
            this.label = label;
        }
    }

    @Getter
    public enum Status{
        PENDING("Chờ xử lý"),
        IN_PROGRESS("Đang xử lý"),
        RESOLVED("Đã hoàn tất"),
        REJECTED("Từ chối");

        private final String label;

        Status(String label) {
            this.label = label;
        }
    }
}

