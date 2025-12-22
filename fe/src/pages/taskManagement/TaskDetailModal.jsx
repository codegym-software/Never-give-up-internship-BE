import React, { useState, useEffect } from "react";
import TaskApi from "../../api/TaskApi";
import "./SprintModal.css";

const Modal = ({ children, onClose, title }) => (
  <div className="modal-overlay" onClick={(e) => {
    if (e.target.className === 'modal-overlay') onClose();
  }}>
    <div className="modal-content">
      <div className="modal-header">
        <h2>{title}</h2>
        <button onClick={onClose} className="close-button">
          &times;
        </button>
      </div>
      {children}
    </div>
  </div>
);

function TaskDetailModal({
  isOpen,
  onClose,
  task,
  teamMembers,
  onTaskUpdated,
}) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [deadline, setDeadline] = useState("");
  const [assigneeId, setAssigneeId] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (task) {
      setName(task.name || "");
      setDescription(task.description || "");
      setDeadline(task.deadline || "");
      setAssigneeId(task.assignee_Id || "");
    }
  }, [task]);

  if (!isOpen || !task) {
    return null;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name) {
      setError("Tên nhiệm vụ là bắt buộc.");
      return;
    }
    setIsSubmitting(true);
    setError("");

    const updatedData = {
      name,
      description,
      deadline: deadline || null,
      assigneeId: assigneeId ? parseInt(assigneeId, 10) : null,
    };

    try {
      await TaskApi.updateTask(task.id, updatedData);
      onTaskUpdated();
      onClose();
    } catch (err) {
      setError("Cập nhật không thành công. Vui lòng thử lại.");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal onClose={onClose} title={`Chỉnh sửa Task: ${task.name}`}>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        <div className="form-group">
          <label>Tên Task <span style={{ color: 'red' }}>*</span></label>
          <input
            type="text"
            className="form-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Tên nhiệm vụ"
          />
        </div>

        <div className="form-group">
          <label>Mô tả</label>
          <textarea
            className="form-input"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Mô tả chi tiết..."
          />
        </div>

        <div className="form-row">
          <div className="form-group" style={{ flex: 1 }}>
            <label>Hạn hoàn thành</label>
            <input
              type="date"
              className="form-input"
              value={deadline}
              onChange={(e) => setDeadline(e.target.value)}
            />
          </div>
          <div className="form-group" style={{ flex: 1 }}>
            <label>Phân công</label>
            <select
              className="form-input"
              value={assigneeId}
              onChange={(e) => setAssigneeId(e.target.value)}
            >
              <option value="">-- Chưa được phân công --</option>
              {teamMembers.map((member) => (
                <option key={member.id} value={member.id}>
                  {member.fullName}
                </option>
              ))}
            </select>
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="modal-actions">
          <button
            type="button"
            className="btn btn-secondary"
            onClick={onClose}
            disabled={isSubmitting}
          >
            Đóng
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Đang lưu..." : "Lưu thay đổi"}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default TaskDetailModal;