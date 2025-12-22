import React, { useState } from "react";
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

function CreateTaskModal({
  isOpen,
  onClose,
  sprintId,
  initialStatus,
  teamMembers,
  onTaskCreated,
}) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [deadline, setDeadline] = useState("");
  const [assigneeId, setAssigneeId] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  if (!isOpen) {
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

    const taskData = {
      name,
      description,
      deadline: deadline || null,
      assigneeId: assigneeId ? parseInt(assigneeId, 10) : null,
      status: initialStatus,
    };

    try {
      await TaskApi.createTask(sprintId, taskData);
      onTaskCreated();
      handleClose();
    } catch (err) {
      setError("Tạo Task thất bại. Vui lòng thử lại.");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = () => {
    setName("");
    setDescription("");
    setDeadline("");
    setAssigneeId("");
    setError("");
    onClose();
  };

  return (
    <Modal onClose={handleClose} title="Tạo Task Mới">
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        <div className="form-group">
          <label>Tên Task <span style={{ color: 'red' }}>*</span></label>
          <input
            type="text"
            className="form-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Nhập tên nhiệm vụ..."
            autoFocus
          />
        </div>

        <div className="form-group">
          <label>Mô tả</label>
          <textarea
            className="form-input"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Mô tả chi tiết nhiệm vụ..."
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
            onClick={handleClose}
            disabled={isSubmitting}
          >
            Đóng
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Đang tạo..." : "Tạo Task"}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default CreateTaskModal;