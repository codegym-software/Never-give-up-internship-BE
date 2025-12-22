import React, { useState, useEffect } from "react";
import LogFilters from "./LogFilters";
import LogTable from "./LogTable";
import LogDetailModal from "./LogDetailModal";
import { LogService } from "~/services/LogService";
import Pagination from "~/components/Pagination";
import { useSearchParams } from "react-router-dom";

const LogManagement = () => {
    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [selectedLog, setSelectedLog] = useState(null);
    const [searchParams, setSearchParams] = useSearchParams();

    // State Phân trang
    const [pagination, setPagination] = useState({
        pageNumber: 1,
        totalElements: 0,
        totalPages: 1,
        hasNext: false,
        hasPrevious: false,
    });

    // State Filter (lưu trữ giá trị từ UI)
    const [filters, setFilters] = useState({
        searchName: searchParams.get("searchName") || "",
        affected: searchParams.get("affected") || "",
        fromDate: searchParams.get("fromDate") || "",
        toDate: searchParams.get("toDate") || "",
    });

    // State Filter Applied (chỉ thay đổi khi bấm Tìm kiếm hoặc chuyển trang)
    const [appliedFilters, setAppliedFilters] = useState({
        searchName: searchParams.get("searchName") || "",
        affected: searchParams.get("affected") || "",
        fromDate: searchParams.get("fromDate") || "",
        toDate: searchParams.get("toDate") || "",
        page: parseInt(searchParams.get("page")) || 1,
    });

    // --- EFFECTS ---
    useEffect(() => {
        fetchLogs();
    }, [appliedFilters]);

    useEffect(() => {
        const params = {};
        if (appliedFilters.searchName) params.searchName = appliedFilters.searchName;
        if (appliedFilters.affected)
            params.affected = appliedFilters.affected;
        if (appliedFilters.fromDate) params.fromDate = appliedFilters.fromDate;
        if (appliedFilters.toDate) params.toDate = appliedFilters.toDate;
        if (appliedFilters.page) params.page = appliedFilters.page;
        setSearchParams(params);
    }, [appliedFilters]);

    // --- API CALL ---
    const fetchLogs = async () => {
        setLoading(true);
        // Gọi Service truyền vào filter và page
        const data = await LogService.getLogs(appliedFilters, appliedFilters.page, 10);

        if (data) {
            // Mapping data theo cấu trúc PagedResponse của Backend
            const content = Array.isArray(data) ? data : (data.content || []);
            setLogs(content);

            setPagination({
                pageNumber: data.pageNumber || appliedFilters.page,
                totalElements: data.totalElements || 0,
                totalPages: data.totalPages || 1,
                hasNext: data.hasNext || false,
                hasPrevious: data.hasPrevious || false,
            });
        }
        setLoading(false);
    };

    // --- HANDLERS ---
    const handleSearch = () => {
        // Khi bấm tìm kiếm, reset về trang 1 và cập nhật appliedFilters
        setAppliedFilters({ ...filters, page: 1 });
    };

    const handleReset = () => {
        const resetState = { searchName: "", affected: "", fromDate: "", toDate: "" };
        setFilters(resetState);
        setAppliedFilters({ ...resetState, page: 1 });
    };

    const handlePageChange = (newPage) => {
        setAppliedFilters({ ...appliedFilters, page: newPage });
    };

    return (
        <div className="page-content">
            {/* Component Lọc */}
            <LogFilters
                filters={filters}
                setFilters={setFilters}
                onSearch={handleSearch}
                onReset={handleReset}
            />

            {/* Component Bảng */}
            <LogTable
                logs={logs}
                loading={loading}
                onViewDetail={setSelectedLog}
                pagination={pagination}
            />

            {/* Component Phân trang */}
                <Pagination
                    pagination={pagination}
  currentPage={appliedFilters.page}
                    changePage={handlePageChange}
                    name="bản ghi"
                />

            {/* Modal Chi tiết */}
            {selectedLog && (
                <LogDetailModal
                    data={selectedLog}
                    onClose={() => setSelectedLog(null)}
                />
            )}
        </div>
    );
};

export default LogManagement;