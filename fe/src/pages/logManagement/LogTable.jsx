import React from "react";
import { FileText } from "lucide-react";

const LogTable = ({ logs, loading, onViewDetail, pagination }) => {

  const translateAction = (action) => {
    if (!action) return "";
    switch (action) {
      case "CREATE": return "Tạo mới";
      case "MODIFY": return "Cập nhật";
      case "DELETE": return "Xóa";
      default: return action;
    }
  };

  const translateModel = (model) => {
    if (!model) return "";
    switch (model) {
      case "ALLOWANCE": return "Phụ cấp";
      case "ATTENDANCE": return "Chấm công";
      case "CHAT_MESSAGE": return "Tin nhắn";
      case "CONVERSATION": return "Hội thoại";
      case "DEPARTMENT": return "Phòng ban";
      case "INTERN": return "Thực tập sinh";
      case "INTERNSHIP_APPLICATION": return "Đơn ứng tuyển";
      case "INTERNSHIP_PROGRAM": return "Chương trình thực tập";
      case "LEAVE_REQUEST": return "Yêu cầu nghỉ phép";
      case "MAJOR": return "Chuyên ngành";
      case "MENTOR": return "Người hướng dẫn";
      case "PENDING_USER": return "User chờ duyệt";
      case "SPRINT": return "Sprint (Nhiệm vụ)";
      case "SUPPORT_REQUEST": return "Yêu cầu hỗ trợ";
      case "TASK": return "Công việc (Task)";
      case "TEAM": return "Nhóm (Team)";
      case "UNIVERSITY": return "Trường đại học";
      case "USER": return "Người dùng hệ thống";
      case "WORK_SCHEDULE": return "Lịch làm việc";
      default: return model;
    }
  };

  const getIndex = (index) => {
    return (pagination.pageNumber - 1) * 10 + index + 1;
  };

  return (
    <div className="table-container">
      <table className="intern-table">
        <thead>
          <tr>
            <th>STT</th>
            <th>Người thực hiện</th>
            <th>Hành động</th>
            <th>Đối tượng</th>
            <th>Mô tả hành động</th>
            <th>Thời gian</th>
            <th>Chi tiết</th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <tr><td colSpan={7} className="text-center">Đang tải dữ liệu...</td></tr>
          ) : logs.length === 0 ? (
            <tr><td colSpan={7} className="text-center">Không có nhật ký hoạt động nào</td></tr>
          ) : (
            logs.map((log, index) => (
              <tr key={log.id}>
                <td>{getIndex(index)}</td>
                <td>
                  <div style={{ fontWeight: 500 }}>{log.actionerName}</div>
                  <div style={{ fontSize: "12px", color: "#6b7280" }}>{log.actionerEmail}</div>
                </td>

                <td>
                  <span className={`status-badge ${log.actionType?.toLowerCase()}`}>
                    {translateAction(log.actionType)}
                  </span>
                </td>

                <td>
                  {translateModel(log.affectedObject)}
                </td>

                <td>
                  <div className="truncate-text" title={log.description}>
                    {log.description && log.description.length > 50
                      ? log.description.substring(0, 50) + "..."
                      : log.description || "-"}
                  </div>
                </td>

                <td>{log.actionAt}</td>

                <td>
                  <button className="icon-btn" onClick={() => onViewDetail(log)}>
                    <FileText size={16} />
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default LogTable;