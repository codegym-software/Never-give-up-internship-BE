package com.example.InternShip.dto.report.response;

import com.example.InternShip.entity.Intern;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FinalReportResponse {
    private Integer internId;
    private String fullName;
    private String email;

    //Các cột điểm đánh giá
    private BigDecimal expertiseScore; //điểm chuyên môn
    private BigDecimal qualityScore; //điểm chất lượng
    private BigDecimal problemSolvingScore; //điểm giải quyết vấn đề
    private BigDecimal technologyLearningScore; //điểm học công nghệ mới
    private BigDecimal averageScore; // điểm trung bình
    private BigDecimal attendancePercent; // tỉ lệ chuyên cần
    private Intern.SoftSkill softSkill; //kỹ năng mềm
    private String assessment; //mentor đánh giá
    private Intern.Status status; //trạng thái
}