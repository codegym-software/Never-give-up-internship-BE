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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Vì nó tự sinh ra nhiều nên sài cái này
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "actioner_id", nullable = false)
    private User action; // người thực hiện

    private String dataChange; // bị thay đổi

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action actionType; // loại hành động

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Model affected; // tên đối tượng bị tác động

    @Column(nullable = false)
    private LocalDateTime actionAt; // thời gian thực hiện

    public enum Action {
        CREATE,
        MODIFY,
        DELETE
    }

    public enum Model {
        ALLOWANCE,
        ATTENDANCE,
        CHAT_MESSAGE,
        CONVERSATION,
        DEPARTMENT,
        INTERN,
        INTERNSHIP_APPLICATION,
        INTERNSHIP_PROGRAM,
        LEAVE_REQUEST,
        MAJOR,
        MENTOR,
        PENDING_USER,
        SPRINT,
        SUPPORT_REQUEST,
        TASK,
        TEAM,
        UNIVERSITY,
        USER
    }
}
