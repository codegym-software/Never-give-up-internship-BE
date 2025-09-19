package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Demo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String hoTen;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    private LocalDate ngaySinh;

    @Column(length = 10, nullable = false)
    private String gioiTinh;
}
