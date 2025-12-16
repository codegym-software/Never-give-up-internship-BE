package com.example.InternShip.service;

import com.example.InternShip.dto.log.response.LogResponse;
import com.example.InternShip.dto.log.response.UserSearchResponse;
import com.example.InternShip.dto.response.PagedResponse;
import com.example.InternShip.entity.Log.Model;

import java.time.LocalDate;
import java.util.List;

public interface LogService {
    PagedResponse<LogResponse> getActivityLogs(
            LocalDate fromDate,
            LocalDate toDate,
            Model affected,
            String searchName,
            int page,
            int size
    );

    List<UserSearchResponse> searchPerformers(String keyword);
}
