package com.example.InternShip.dto.log.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogResponse {
    private Integer id;

    // Thông tin người thực hiện
    private Integer actionerId;
    private String actionerName;
    private String actionerEmail;
    private String actionerRole;

    // Thông tin hành động
    private String actionType; // CREATE, MODIFY, DELETE
    private String affectedObject; // INTERN, TEAM...
    private String description;    // Mô tả hành động
    private String dataOld;        // Dữ liệu cũ (JSON)
    private String dataNew;        // Dữ liệu mới (JSON)

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime actionAt;
}