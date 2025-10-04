package com.example.InternShip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Intern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private University university;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    //
    @Getter
    public enum Status{
        ACTIVE, // đang thực tập
        SUSPENDED, // tạm dừng thực tập
        COMPLETED, // hoành thành thực tập
        DROPPED; // dừng thực tập
    }
}
