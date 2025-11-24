package com.example.InternShip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Vì nó tự sinh ra nhiều nên sài cái này
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "actioner_id", nullable = false)
    private User action; // người thực hiện

    private String oldData; // Dữ liệu cũ

    private String newData; // Dữ liệu mới

    @Column(nullable = false)
    private Action actionType; // loại hành động

    @Column(nullable = false)
    private LocalDateTime actionAt; // thời gian thực hiện

    public enum Action {
        CREATE,
        MODIFY,
        DELETE
    }
}
