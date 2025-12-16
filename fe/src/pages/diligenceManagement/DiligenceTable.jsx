import { FileText, ImageOff } from "lucide-react";
import DiligenceDetail from "./DiligenceDetail";
import { useState } from "react";

const DiligenceTable = ({ data, loading }) => {
  const [showForm, setShowForm] = useState(false);
  const [intern, setIntern] = useState({});

  const showDetail = (data) => {
    setIntern(data);
    setShowForm(true);
  };

  return (
    <>
      <div className="table-container">
        <table className="intern-table">
          <thead>
            <tr>
              <th>Họ tên</th>
              <th>Email</th>
              <th>Tên nhóm</th>
              <th>Tổng ngày làm</th>
              <th>Có mặt</th>
              <th>Vắng mặt</th>
              <th>Nghỉ phép</th>
              <th>Không check-out</th>
              <th>Sai giờ</th>
              <th>Sai giờ (phép)</th>
              <th>Tỉ lệ chuyên cần</th>
              <th className="action-col"></th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan="12" className="text-center">
                  Đang tải...
                </td>
              </tr>
            ) : data.length === 0 ? (
              <tr>
                <td colSpan="12" className="text-center">
                  Không có dữ liệu
                </td>
              </tr>
            ) : (
              data.map((data) => (
                <tr key={data.internId}>
                  <td>{data.fullName}</td>
                  <td>{data.email}</td>
                  <td>{data.teamName || "-"}</td>
                  <td>{data.totalWorkDays}</td>
                  <td>{data.totalPresentDays}</td>
                  <td>{data.totalAbsentDays}</td>
                  <td>{data.totalOnLeaveDays}</td>
                  <td>{data.totalCheckedInDays}</td>
                  <td>{data.totalTimeViolationDays}</td>
                  <td>{data.totalExcusedTimeDays}</td>
                  <td>{data.attendancePercent}%</td>
                  <td className="action-col">
                    <button
                      className="icon-btn"
                      onClick={() => showDetail(data)}
                    >
                      <FileText size={15} />
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {showForm && (
        <DiligenceDetail onClose={() => setShowForm(false)} intern={intern} />
      )}
    </>
  );
};

export default DiligenceTable;
