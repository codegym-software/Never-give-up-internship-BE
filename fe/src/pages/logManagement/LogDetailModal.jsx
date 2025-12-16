import React, { useMemo } from "react";
import { X, ArrowRight } from "lucide-react";

const LogDetailModal = ({ data, onClose }) => {
  // Memoize việc format để tránh tính toán lại. Hooks phải được gọi ở top-level.
  // Sử dụng optional chaining (?.) để tránh lỗi khi data ban đầu là null.
  const oldDataFormatted = useMemo(() => formatJson(data?.dataOld), [data?.dataOld]);
  const newDataFormatted = useMemo(() => formatJson(data?.dataNew), [data?.dataNew]);

  // return có điều kiện phải được đặt sau khi tất cả các hooks đã được gọi.
  if (!data) return null;

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

  // Hàm format JSON an toàn
  const formatJson = (jsonString) => {
    if (!jsonString) return null;
    try {
      // Nếu là JSON object thì parse để format đẹp
      const parsed = JSON.parse(jsonString);
      return JSON.stringify(parsed, null, 2);
    } catch {
      // Nếu là string thường thì trả về nguyên bản
      return jsonString;
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal" style={{ maxWidth: "900px", width: "90%" }}> {/* Tăng chiều rộng để hiển thị 2 cột */}
        <div className="modal-header">
          <h3>Chi tiết nhật ký #{data.id}</h3>
          <button className="modal-close" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          {/* 1. THÔNG TIN CHUNG */}
          <div className="detail-grid" style={{ display: "grid", gap: "12px", fontSize: "14px", borderBottom: "1px solid #eee", paddingBottom: "15px", marginBottom: "15px" }}>
            <div style={{ display: "flex", justifyContent: "space-between" }}>
                <div>
                    <strong>Người thực hiện:</strong> {data.actionerName} <span className="text-gray">({data.actionerEmail})</span>
                </div>
                <div>
                    <strong>Thời gian:</strong> {data.actionAt}
                </div>
            </div>

            <div style={{ display: "flex", gap: "30px", alignItems: "center" }}>
              <div>
                <strong>Hành động: </strong>
                <span className={`status-badge ${data.actionType?.toLowerCase()}`} style={{marginLeft: "5px"}}>
                  {translateAction(data.actionType)}
                </span>
              </div>
              <div>
                <strong>Đối tượng: </strong>
                <span className="font-medium">{translateModel(data.affectedObject)}</span>
              </div>
            </div>
          </div>

          {/* 2. MÔ TẢ HÀNH ĐỘNG */}
          {data.description && (
            <div style={{ 
              padding: "12px", 
              backgroundColor: "#eef2ff", 
              borderLeft: "4px solid #6366f1",
              borderRadius: "4px",
              marginBottom: "20px",
              color: "#333",
              fontSize: "15px",
              fontWeight: "500"
            }}>
              {data.description}
            </div>
          )}

          {/* 3. SO SÁNH DỮ LIỆU CŨ - MỚI */}
          <div style={{ display: "grid", gridTemplateColumns: "1fr auto 1fr", gap: "10px", alignItems: "start" }}>
            
            {/* Cột Dữ Liệu Cũ */}
            <div style={{ display: "flex", flexDirection: "column" }}>
                <strong style={{ color: "#b91c1c", marginBottom: "5px" }}>Dữ liệu trước (Old):</strong>
                <div style={{ 
                    backgroundColor: "#fff1f2", 
                    border: "1px solid #fda4af", 
                    borderRadius: "6px", 
                    padding: "10px",
                    minHeight: "100px"
                }}>
                    {oldDataFormatted ? (
                        <pre style={{ margin: 0, fontSize: "12px", whiteSpace: "pre-wrap", color: "#881337" }}>
                            {oldDataFormatted}
                        </pre>
                    ) : (
                        <span className="text-gray italic" style={{ fontSize: "13px" }}>Không có dữ liệu cũ (Tạo mới hoặc không tìm thấy)</span>
                    )}
                </div>
            </div>

            {/* Mũi tên ở giữa */}
            <div style={{ paddingTop: "40px", color: "#94a3b8" }}>
                <ArrowRight size={24} />
            </div>

            {/* Cột Dữ Liệu Mới */}
            <div style={{ display: "flex", flexDirection: "column" }}>
                <strong style={{ color: "#15803d", marginBottom: "5px" }}>Dữ liệu sau (New):</strong>
                <div style={{ 
                    backgroundColor: "#f0fdf4", 
                    border: "1px solid #86efac", 
                    borderRadius: "6px", 
                    padding: "10px",
                    minHeight: "100px"
                }}>
                    {newDataFormatted ? (
                        <pre style={{ margin: 0, fontSize: "12px", whiteSpace: "pre-wrap", color: "#14532d" }}>
                            {newDataFormatted}
                        </pre>
                    ) : (
                        <span className="text-gray italic" style={{ fontSize: "13px" }}>Không có dữ liệu mới (Đã xóa)</span>
                    )}
                </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  );
};

export default LogDetailModal;