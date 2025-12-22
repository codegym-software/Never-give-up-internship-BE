import React, { useState, useEffect } from "react";
import SprintApi from "../../api/SprintApi";
import "./SprintModal.css";

// Re-using the simple modal structure (or better yet, extract Modal to a common component, but keeping it local as per instruction context)
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

function EditSprintModal({ isOpen, onClose, sprint, onSprintUpdated, onDeleteRequest }) {
  const [name, setName] = useState("");
  const [goal, setGoal] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (sprint) {
      setName(sprint.name || "");
      setGoal(sprint.goal || "");
      setStartDate(sprint.startDate || "");
      setEndDate(sprint.endDate || "");
    }
  }, [sprint]);

  if (!isOpen) {
    return null;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError("");

    const sprintData = { name, goal, startDate, endDate };

    try {
      await SprintApi.update(sprint.id, sprintData);
      onSprintUpdated();
      onClose();
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Cập nhật sprint không thành công. Vui lòng kiểm tra lại ngày tạo.";
      setError(errorMessage);
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = () => {
    if (window.confirm("Bạn có chắc chắn muốn xóa Sprint này không? Hành động này không thể hoàn tác.")) {
        onDeleteRequest(sprint.id);
    }
  };

  return (
    <Modal onClose={onClose} title={`Chỉnh sửa Sprint: ${sprint?.name}`}>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        <div className="form-group">
          <label htmlFor="sprintName">Tên Sprint</label>
          <input
            id="sprintName"
            type="text"
            className="form-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Tên của sprint"
          />
        </div>

        <div className="form-group">
          <label htmlFor="sprintGoal">Mục tiêu</label>
          <textarea
            id="sprintGoal"
            className="form-input"
            value={goal}
            onChange={(e) => setGoal(e.target.value)}
            placeholder="Mục tiêu của sprint"
          />
        </div>

        <div className="form-row">
          <div className="form-group" style={{ flex: 1 }}>
            <label htmlFor="startDate">Ngày bắt đầu</label>
            <input
              id="startDate"
              type="date"
              className="form-input"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>
          <div className="form-group" style={{ flex: 1 }}>
            <label htmlFor="endDate">Ngày kết thúc</label>
            <input
              id="endDate"
              type="date"
              className="form-input"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="modal-actions" style={{ justifyContent: 'space-between' }}>
          <button
            type="button"
            className="btn btn-danger"
            onClick={handleDelete}
            disabled={isSubmitting}
          >
            Xóa Sprint
          </button>
          
          <div style={{ display: 'flex', gap: '10px' }}>
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
        </div>
      </form>
    </Modal>
  );
}

export default EditSprintModal;