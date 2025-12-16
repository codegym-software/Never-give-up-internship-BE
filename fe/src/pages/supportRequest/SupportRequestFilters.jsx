import React from "react";
import { Search } from "lucide-react";
import Select from "react-select";

const statusOptions = [
  { value: "", label: "Tất cả" },
  { value: "PENDING", label: "Chờ xử lý" },
  { value: "RESOLVED", label: "Đã duyệt" },
  { value: "REJECTED", label: "Đã từ chối" },
];

const SupportRequestFilters = ({
  filters,
  handleFilterChange,
  handleSearch,
}) => {
  return (
    <div className="filter-container">
      <div className="filter-grid">
        {/* SEARCH */}
        <div className="filter-item">
          <label className="filter-label">Tìm theo tên hoặc email</label>
          <div className="search-input-wrapper">
            <Search size={16} className="search-icon" />
            <input
              className="search-input"
              placeholder="Nhập từ khóa..."
              value={filters.search}
              onChange={(e) => handleFilterChange("search", e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleSearch()}
            />
          </div>
        </div>

        {/* STATUS FILTER */}
        <div className="filter-item">
          <label className="filter-label">Trạng thái</label>
          <Select
            value={statusOptions.find((opt) => opt.value === filters.status)}
            onChange={(opt) =>
              handleFilterChange("status", opt ? opt.value : "")
            }
            options={statusOptions}
            placeholder="Chọn trạng thái"
            isClearable={false}
          />
        </div>

        <button className="btn btn-search" onClick={handleSearch}>
          Lọc dữ liệu
        </button>
      </div>
    </div>
  );
};

export default SupportRequestFilters;
