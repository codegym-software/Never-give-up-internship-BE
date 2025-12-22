import { useState, useEffect } from "react";
import DiligenceFilters from "./DiligenceFilters";
import DiligenceTable from "./DiligenceTable";
import { useSearchParams } from "react-router-dom";
import { getAttendanceSummary } from "~/services/ReportService";
import Pagination from "~/components/Pagination";

const DiligenceManagement = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();

  const [filters, setFilters] = useState({
    teamId: parseInt(searchParams.get("teamId")) || 0,
    internshipProgramId: parseInt(searchParams.get("internshipProgramId")) || 0,
    page: parseInt(searchParams.get("page")) || 1,
  });

  const [appliedFilters, setAppliedFilters] = useState({
    teamId: parseInt(searchParams.get("teamId")) || 0,
    internshipProgramId: parseInt(searchParams.get("internshipProgramId")) || 0,
    page: parseInt(searchParams.get("page")) || 1,
  });

  const [pagination, setPagination] = useState({
    pageNumber: 1,
    totalElements: 0,
    totalPages: 1,
    hasNext: false,
    hasPrevious: false,
  });

  useEffect(() => {
    const params = {};
    if (appliedFilters.teamId) params.teamId = appliedFilters.teamId;
    if (appliedFilters.internshipProgramId)
      params.internshipProgramId = appliedFilters.internshipProgramId;
    if (appliedFilters.page) params.page = appliedFilters.page;
    setSearchParams(params);
  }, [appliedFilters]);

  const fetchData = async () => {
    setLoading(true);
    const data = await getAttendanceSummary(appliedFilters);
    setData(data.content);
    setPagination({
      pageNumber: data.pageNumber,
      totalElements: data.totalElements,
      totalPages: data.totalPages,
      hasNext: data.hasNext,
      hasPrevious: data.hasPrevious,
    });
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, [appliedFilters]);

  const handleSearch = () => {
    setAppliedFilters({ ...filters, page: 1 });
  };

  return (
    <>
      <DiligenceFilters
        filters={filters}
        handleFilterChange={(key, value) =>
          setFilters({ ...filters, [key]: value })
        }
        handleSearch={handleSearch}
      />

      <DiligenceTable data={data} loading={loading} />

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

export default DiligenceManagement;
