import React from "react";

const formatSoftSkill = (skill) => {
    switch (skill) {
      case "GOOD":
        return "Tốt";
      case "FAIR":
        return "Khá";
      case "AVERAGE":
        return "Trung bình";
      case "POOR":
        return "Kém";      
    }
  };

const Table = ({ reports, loading }) => (
  <div className="table-container">
    <table className="intern-table">
      <thead>
        <tr>
          <th>Họ và tên</th>
          <th>Email</th>
          <th>Chuyên môn</th>
          <th>Chất lượng</th>
          <th>Tư duy</th>
          <th>Học hỏi</th>
          <th>Điểm TB</th>
          <th>Kĩ năng mềm</th>
          <th>Chuyên cần</th>
          <th>Nhận xét</th>
          <th>Trạng thái</th>
        </tr>
      </thead>
      <tbody>
        {loading ? (
          <tr>
            <td colSpan="11" className="text-center">
              Đang tải...
            </td>
          </tr>
        ) : reports.length === 0 ? (
          <tr>
            <td colSpan="11" className="text-center">
              Không có dữ liệu
            </td>
          </tr>
        ) : (
          reports.map((intern) => (
            <tr key={intern.internId}>
              <td>{intern.fullName}</td>
              <td>{intern.email}</td>
              <td>{intern.expertiseScore || "-"}</td>
              <td>{intern.qualityScore || "-"}</td>
              <td>{intern.problemSolvingScore || "-"}</td>
              <td>{intern.technologyLearningScore || "-"}</td>
              <td>{intern.averageScore || "-"}</td>
              <td>{formatSoftSkill(intern.softSkill) || "-"}</td>
              <td>{intern.attendancePercent}%</td>
              <td>{intern.assessment || "-"}</td>
              <td>
                <span className={`status-badge ${intern.status.toLowerCase()}`}>
                  {intern.status}
                </span>
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  </div>
);

export default Table;
