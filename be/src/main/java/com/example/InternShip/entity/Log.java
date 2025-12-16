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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "actioner_id", nullable = false)
    private User action; // người thực hiện

    @Column(columnDefinition = "TEXT")
    private String dataOld; // Dữ liệu CŨ (Trước khi thay đổi)

    @Column(columnDefinition = "TEXT")
    private String dataNew; // Dữ liệu MỚI (Sau khi thay đổi)

    private String description; //Mô tả ngắn gọn

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
        USER,
        WORK_SCHEDULE,
    }
}
