package com.example.InternShip.dto.supportRequest.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSupportRequestReponse {
    private Integer id;
    private String title;
    private String description;
    private String evidenceFileUrl;
    private String internName;
    private String internEmail;
    private String handlerName;
    private String hrResponse;
    private LocalDateTime createAt;
    private LocalDateTime resolvedAt;
    private String status;
}
