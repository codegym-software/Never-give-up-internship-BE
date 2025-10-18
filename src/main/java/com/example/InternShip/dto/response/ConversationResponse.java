package com.example.InternShip.dto.response;

import com.example.InternShip.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversationResponse {
    private Long id;
    private String candidateName;
    private User hr;
    private String lastMessage;
    private LocalDateTime lastMessageTimestamp;
    private int unreadCount;
}
