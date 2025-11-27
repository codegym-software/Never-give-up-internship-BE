package com.example.InternShip.dto.report.response;

import com.example.InternShip.entity.Intern;
import lombok.Data;

import java.util.Map;

@Data
public class ChartResponse {
    private ScoreChart scoreChart;
    private Map<Intern.Status, Integer> statusCounts;

    @Data
    public static class ScoreChart {
        private int scoreA; // High score (>=8)
        private int scoreB; // Mid score (6.5 - 7.9)
        private int scoreC; // Low score (<6.5)
    }
}
