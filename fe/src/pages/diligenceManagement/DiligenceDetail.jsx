import { useEffect, useState } from "react";
import { getInternAttendanceDetail } from "~/services/ReportService";
import { X } from "lucide-react";

const DiligenceDetail = ({ onClose, intern }) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const translateStatus = (status) => {
    switch (status) {
      case "CHECKED_IN":
        return "Đã check-in";
      case "PRESENT":
        return "Có mặt";
      case "TIME_VIOLATION":
        return "Đi muộn/về sớm";
      case "EXCUSED_TIME":
        return "Đi muộn/về sớm có phép";
      case "ON_LEAVE":
        return "Nghỉ có phép";
      case "ABSENT":
        return "Vắng mặt";
      default:
        return status;
    }
  };

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      const response = await getInternAttendanceDetail(intern.internId);
      setData(response);
      setLoading(false);
    };
    load();
  }, []);

  const toString = (leaveLogs) =>
    leaveLogs.map((item) => `${item.type}(${item.leaveStatus})`).join(", ");

  return (
    <div className="modal-overlay">
      <div className="modal-detail">
        <div className="modal-header">
          <h3>Chi tiết chấm công của {intern.fullName}</h3>
          <button className="modal-close" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="table-container">
          <table className="intern-table">
            <thead>
              <tr>
                <th>Ngày</th>
                <th>Giờ làm</th>
                <th>Check-in</th>
                <th>Check-out</th>
                <th>Xin phép</th>
                <th>Trạng thái</th>
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
                  <td colSpan="6" className="text-center">
                    Không có dữ liệu
                  </td>
                </tr>
              ) : (
                data.map((intern) => (
                  <tr>
                    <td>{intern.date}</td>
                    <td>{`${intern.expectedTimeStart} - ${intern.expectedTimeEnd}`}</td>
                    <td>{intern.actualCheckIn}</td>
                    <td>{intern.actualCheckOut}</td>
                    <td>
                      {intern.leaveLogs.length > 0
                        ? toString(intern.leaveLogs)
                        : "-"}
                    </td>
                    <td>
                      <span
                        className={`status-badge ${intern.status.toLowerCase()}`}
                      >
                        {translateStatus(intern.status)}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default DiligenceDetail;
