import React, { useMemo } from "react";
import { X, ArrowRight } from "lucide-react";

const formatJson = (jsonString) => {
  if (!jsonString) return null;
  try {
    const parsed = JSON.parse(jsonString);
    return JSON.stringify(parsed, null, 2);
  } catch {
    return jsonString;
  }
};

const LogDetailModal = ({ data, onClose }) => {
  const oldDataFormatted = useMemo(() => data ? formatJson(data.dataOld) : null, [data?.dataOld]);
  const newDataFormatted = useMemo(() => data ? formatJson(data.dataNew) : null, [data?.dataNew]);

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

  const translateRole = (role) => {
    if (!role) return "";
    switch (role) {
      case "ADMIN": return "Quản trị viên";
      case "MENTOR": return "Giảng viên/Mentor";
      case "INTERN": return "Thực tập sinh";
      case "HR": return "Nhân sự";
      case "VISITOR": return "Khách";
      default: return role;
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

  return (
    <div className="modal-overlay">
      <div className="modal" style={{ maxWidth: "900px", width: "90%" }}>
        <div className="modal-header">
          <h3>Chi tiết nhật ký</h3>
          <button className="modal-close" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          {/* 1. THÔNG TIN CHUNG */}
          <div className="detail-grid" style={{ display: "grid", gap: "12px", fontSize: "14px", borderBottom: "1px solid #eee", paddingBottom: "15px", marginBottom: "15px" }}>
            <div style={{ display: "flex", justifyContent: "space-between" }}>
                <div>
                    <strong>Người thực hiện:</strong> {data.actionerName} 
                    
                    {/* --- 2. HIỂN THỊ ROLE (BADGE) --- */}
                    {data.actionerRole && (
                        <span style={{ 
                            marginLeft: "8px", 
                            padding: "2px 8px", 
                            backgroundColor: "#e0e7ff",
                            color: "#4338ca",           
                            borderRadius: "4px", 
                            fontSize: "12px", 
                            fontWeight: "600",
                            border: "1px solid #c7d2fe",
                            display: "inline-block",
                            verticalAlign: "middle"
                        }}>
                            {translateRole(data.actionerRole)}
                        </span>
                    )}

                    <span className="text-gray" style={{ marginLeft: "6px" }}>({data.actionerEmail})</span>
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
                    minHeight: "100px",
                    maxHeight: "300px",
                    overflowY: "auto"
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
                    minHeight: "100px",
                    maxHeight: "300px",
                    overflowY: "auto"
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