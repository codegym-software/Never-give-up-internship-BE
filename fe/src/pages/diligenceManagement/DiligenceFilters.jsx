import { useEffect, useState } from "react";
import React from "react";
import { Search } from "lucide-react";
import Select from "react-select";
import { getAllInternshipProgram } from "~/services/InternshipProgramService";
import { getTeamsByIP } from "~/services/TeamService";

const DiligenceFilters = ({ filters, handleFilterChange, handleSearch }) => {
  const [internshipProgram, setInternshipProgram] = useState([]);
  const [teams, setTeams] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const internshipProgramData = await getAllInternshipProgram();
      setInternshipProgram([
        { id: 0, name: "Tất cả" },
        ...internshipProgramData,
      ]);
    };
    fetchData();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      if (filters.internshipProgramId === 0) {
        setTeams([{ id: 0, name: "Tất cả" }]);
        return;
      }
      const teamData = await getTeamsByIP(filters.internshipProgramId);
      setTeams([{ id: 0, name: "Tất cả" }, ...teamData]);
    };
    fetchData();
    handleFilterChange("teamId", 0);
  }, [filters.internshipProgramId]);

  const internshipProgramOptions = internshipProgram.map((u) => ({
    value: u.id,
    label: u.name,
  }));

  const teamOptions = teams.map((u) => ({
    value: u.id,
    label: u.name,
  }));

  return (
    <>
      {/* FILTERS */}
      <div className="filter-container">
        <div className="filter-grid">
          <div className="filter-item">
            <label className="filter-label">Kì thực tập</label>
            <Select
              className="custom-select"
              options={internshipProgramOptions}
              value={internshipProgramOptions.find(
                (opt) => opt.value === filters.internshipProgramId
              )}
              onChange={(selected) => {
                handleFilterChange("internshipProgramId", selected.value);
              }}
            />
          </div>

          <div className="filter-item">
            <label className="filter-label">Nhóm</label>
            <Select
              className="custom-select"
              options={teamOptions}
              value={teamOptions.find((opt) => opt.value === filters.teamId)}
              onChange={(selected) =>
                handleFilterChange("teamId", selected.value)
              }
              isDisabled={filters.internshipProgramId === 0}
            />
          </div>

          <button onClick={handleSearch} className="btn btn-search">
            <Search size={18} /> Tìm kiếm
          </button>
        </div>
      </div>
    </>
  );
};

export default DiligenceFilters;
