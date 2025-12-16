// src/components/evaluation/Filter.jsx
import React from "react";
import { Search, Download } from "lucide-react";
import Select from "react-select";
import * as XLSX from "xlsx";

const Filters = ({
  filters,
  universities,
  internshipProgram,
  handleFilterChange,
  handleSearch,
  reports,
}) => {
  const universityOptions = universities.map((u) => ({
    value: u.id,
    label: u.name,
  }));

  const internshipProgramOptions = internshipProgram.map((m) => ({
    value: m.id,
    label: m.name,
  }));

  const exportToExcel = () => {
    if (!reports || reports.length === 0) {
      alert("Không có dữ liệu để xuất Excel!");
      return;
    }

    const data = reports.map((i) => ({
      "Họ tên": i.fullName,
      Email: i.email ?? "N/A",
      "Điểm chuyên môn": i.expertiseScore ?? "N/A",
      "Điểm chất lượng": i.qualityScore ?? "N/A",
      "Kỹ năng giải quyết vấn đề": i.problemSolvingScore ?? "N/A",
      "Học hỏi": i.technologyLearningScore ?? "N/A",
      "Điểm trung bình": i.averageScore ?? "N/A",
      "Tỉ lệ chuyên cần": i.attendancePercent ?? "N/A",
      "Thái độ & kỹ năng mềm":
        i.softSkill === "GOOD"
          ? "Tốt"
          : i.softSkill === "FAIR"
          ? "Khá"
          : i.softSkill === "AVERAGE"
          ? "Trung bình"
          : i.softSkill === "POOR"
          ? "Kém"
          : "N/A",
      "Nhận xét": i.assessment || "N/A",
    }));

    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Đánh giá TTS");

    XLSX.writeFile(workbook, `Tong_hop_bao_cao.xlsx`);
  };

  return (
    <>
      <div className="filter-container">
        <div className="filter-grid">
          <div className="filter-item">
            <label className="filter-label">Trường học</label>
            <Select
              className="custom-select"
              options={universityOptions}
              value={universityOptions.find(
                (opt) => opt.value === filters.universityId
              )}
              onChange={(selected) =>
                handleFilterChange("universityId", selected.value)
              }
            />
          </div>

          <div className="filter-item">
            <label className="filter-label">Kì thực tập</label>
            <Select
              className="custom-select"
              options={internshipProgramOptions}
              value={internshipProgramOptions.find(
                (opt) => opt.value === filters.internshipProgramId
              )}
              onChange={(selected) =>
                handleFilterChange("internshipProgramId", selected.value)
              }
            />
          </div>

          <button onClick={handleSearch} className="btn btn-search">
            <Search size={18} /> Tìm kiếm
          </button>

          <button className="btn btn-add" onClick={exportToExcel}>
            <Download size={18} /> Xuất excel
          </button>
        </div>
      </div>
    </>
  );
};

export default Filters;
