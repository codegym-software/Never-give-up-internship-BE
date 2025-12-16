import React from "react";
import { Search, RefreshCw } from "lucide-react";
import Select from "react-select";

// Danh sách ENUM khớp với Log.java
const affectedOptions = [
  { value: "", label: "Tất cả đối tượng" },
  { value: "ALLOWANCE", label: "Phụ cấp" },
  { value: "ATTENDANCE", label: "Chấm công" },
  { value: "CHAT_MESSAGE", label: "Tin nhắn chat" },
  { value: "CONVERSATION", label: "Cuộc hội thoại" },
  { value: "DEPARTMENT", label: "Phòng ban" },
  { value: "INTERN", label: "Thực tập sinh" },
  { value: "INTERNSHIP_APPLICATION", label: "Đơn ứng tuyển" },
  { value: "INTERNSHIP_PROGRAM", label: "Chương trình thực tập" },
  { value: "LEAVE_REQUEST", label: "Yêu cầu nghỉ phép" },
  { value: "MAJOR", label: "Chuyên ngành" },
  { value: "MENTOR", label: "Người hướng dẫn (Mentor)" },
  { value: "PENDING_USER", label: "Người dùng chờ duyệt" },
  { value: "SPRINT", label: "Sprint (Nhiệm vụ)" },
  { value: "SUPPORT_REQUEST", label: "Yêu cầu hỗ trợ" },
  { value: "TASK", label: "Công việc (Task)" },
  { value: "TEAM", label: "Nhóm (Team)" },
  { value: "UNIVERSITY", label: "Trường đại học" },
  { value: "USER", label: "Người dùng hệ thống" },
  { value: "WORK_SCHEDULE", label: "Lịch làm việc" },
];

const LogFilters = ({ filters, setFilters, onSearch }) => {

  const handleChange = (key, value) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  return (
    <div className="filter-container">
      <div className="filter-grid">

        {/* 1. Tìm theo tên người thực hiện (searchName) */}
        <div className="filter-item">
          <label className="filter-label">Người thực hiện</label>
          <div className="search-input-wrapper">
            <Search size={16} className="search-icon" />
            <input
              className="search-input"
              placeholder="Nhập tên nhân viên..."
              value={filters.searchName}
              onChange={(e) => handleChange("searchName", e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && onSearch()}
            />
          </div>
        </div>

        {/* 2. Lọc theo đối tượng (affected) */}
        <div className="filter-item">
          <label className="filter-label">Đối tượng tác động</label>
          <Select
            className="react-select-container"
            classNamePrefix="react-select"
            options={affectedOptions}
            value={affectedOptions.find((opt) => opt.value === filters.affected)}
            onChange={(opt) => handleChange("affected", opt ? opt.value : "")}
            placeholder="Chọn đối tượng"
            isClearable
          />
        </div>

        {/* 3. Từ ngày (fromDate) */}
        <div className="filter-item">
          <label className="filter-label">Từ ngày</label>
          <input
            type="date"
            className="search-input"
            value={filters.fromDate}
            onChange={(e) => handleChange("fromDate", e.target.value)}
          />
        </div>

        {/* 4. Đến ngày (toDate) */}
        <div className="filter-item">
          <label className="filter-label">Đến ngày</label>
          <input
            type="date"
            className="search-input"
            value={filters.toDate}
            onChange={(e) => handleChange("toDate", e.target.value)}
          />
        </div>

        {/* Nút bấm */}
        <div className="filter-actions">
          <button className="btn btn-search" onClick={onSearch}>
            <Search size={16} /> Tìm kiếm
          </button>
        </div>
      </div>
    </div>
  );
};

export default LogFilters;