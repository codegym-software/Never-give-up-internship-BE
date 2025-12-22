// src/components/evaluation/EvaluationReport.jsx
import React, { useState, useEffect } from "react";
import { getAllUniversity } from "~/services/UniversityService";
import { getAllInternshipProgram } from "~/services/InternshipProgramService";
import { useSearchParams } from "react-router-dom";
import { getFinalReport } from "~/services/ReportService";
import Filters from "./Filter";
import Table from "./Table";
import Pagination from "~/components/Pagination";
import Chart from "./Chart";

const EvaluationReport = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(false);
  const [universities, setUniversities] = useState([]);
  const [internshipProgram, setInternshipProgram] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();

  const [filters, setFilters] = useState({
    universityId: parseInt(searchParams.get("universityId")) || 0,
    internshipProgramId: parseInt(searchParams.get("internshipProgramId")) || 0,
    page: parseInt(searchParams.get("page")) || 1,
  });

  const [appliedFilters, setAppliedFilters] = useState({
    universityId: parseInt(searchParams.get("universityId")) || 0,
    internshipProgramId: parseInt(searchParams.get("internshipProgramId")) || 0,
    page: parseInt(searchParams.get("page")) || 1,
  });

  useEffect(() => {
    const params = {};
    if (appliedFilters.universityId)
      params.universityId = appliedFilters.universityId;
    if (appliedFilters.internshipProgramId)
      params.internshipProgramId = appliedFilters.internshipProgramId;
    if (appliedFilters.page) params.page = appliedFilters.page;

    setSearchParams(params);
  }, [appliedFilters]);

  const [pagination, setPagination] = useState({
    pageNumber: 1,
    totalElements: 0,
    totalPages: 1,
    hasNext: false,
    hasPrevious: false,
  });

  useEffect(() => {
    const fetchData = async () => {
      const [uniData, internshipProgramData] = await Promise.all([
        getAllUniversity(),
        getAllInternshipProgram(),
      ]);

      setUniversities([{ id: 0, name: "Tất cả" }, ...uniData]);
      setInternshipProgram([
        { id: 0, name: "Tất cả" },
        ...internshipProgramData,
      ]);
    };

    fetchData();
  }, []);

  useEffect(() => {
    fetchReports();
  }, [appliedFilters]);

  const fetchReports = async () => {
    setLoading(true);

    const data = await getFinalReport({
      universityId: appliedFilters.universityId,
      internshipProgramId: appliedFilters.internshipProgramId,
      page: appliedFilters.page,
    });

    setReports(data.content);
    setPagination({
      pageNumber: data.pageNumber,
      totalElements: data.totalElements,
      totalPages: data.totalPages,
      hasNext: data.hasNext,
      hasPrevious: data.hasPrevious,
    });

    setLoading(false);
  };

  const handleSearch = () => {
    setAppliedFilters({ ...filters, page: 1 });
  };

  return (
    <>
      <Filters
        filters={filters}
        universities={universities}
        internshipProgram={internshipProgram}
        handleFilterChange={(key, value) =>
          setFilters({ ...filters, [key]: value })
        }
        handleSearch={handleSearch}
        reports={reports} // <-- TRUYỀN REPORTS VÀO ĐỂ EXPORT EXCEL
      />

      <Chart filters={appliedFilters} />

      <Table reports={reports} loading={loading} />

      <Pagination
        pagination={pagination}
  currentPage={appliedFilters.page}
        changePage={(newPage) =>
          setAppliedFilters({ ...appliedFilters, page: newPage })
        }
        name={"thực tập sinh"}
      />
    </>
  );
};

export default EvaluationReport;
