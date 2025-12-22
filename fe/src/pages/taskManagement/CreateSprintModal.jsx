import React, { useState } from "react";
import SprintApi from "../../api/SprintApi";
import "./CreateSprintModal.css";

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

function CreateSprintModal({ isOpen, onClose, teamId, onSprintCreated }) {
  const [name, setName] = useState("");
  const [goal, setGoal] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  if (!isOpen) {
    return null;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name || !startDate || !endDate) {
      setError("Vui lòng nhập đầy đủ Tên sprint, Ngày bắt đầu và Ngày kết thúc.");
      return;
    }
    setIsSubmitting(true);
    setError("");

    const sprintData = { name, goal, startDate, endDate };

    try {
      await SprintApi.create(teamId, sprintData);
      onSprintCreated();
      handleClose();
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Không thể tạo sprint. Vui lòng kiểm tra lại thông tin.";
      setError(errorMessage);
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleClose = () => {
    setName("");
    setGoal("");
    setStartDate("");
    setEndDate("");
    setError("");
    onClose();
  };

  return (
    <Modal onClose={handleClose} title="Tạo Sprint Mới">
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        <div className="form-group">
          <label htmlFor="sprintName">Tên Sprint <span style={{ color: 'red' }}>*</span></label>
          <input
            id="sprintName"
            type="text"
            className="form-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Ví dụ: Sprint 1 - Khởi tạo dự án"
            autoFocus
          />
        </div>

        <div className="form-group">
          <label htmlFor="sprintGoal">Mục tiêu Sprint</label>
          <textarea
            id="sprintGoal"
            className="form-input"
            value={goal}
            onChange={(e) => setGoal(e.target.value)}
            placeholder="Mô tả ngắn gọn mục tiêu cần đạt được trong sprint này..."
          />
        </div>

        <div className="form-row">
          <div className="form-group" style={{ flex: 1 }}>
            <label htmlFor="startDate">Ngày bắt đầu <span style={{ color: 'red' }}>*</span></label>
            <input
              id="startDate"
              type="date"
              className="form-input"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>
          <div className="form-group" style={{ flex: 1 }}>
            <label htmlFor="endDate">Ngày kết thúc <span style={{ color: 'red' }}>*</span></label>
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

        <div className="modal-actions">
          <button 
            type="button" 
            className="btn btn-secondary" 
            onClick={handleClose} 
            disabled={isSubmitting}
          >
            Hủy bỏ
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Đang xử lý..." : "Tạo Sprint"}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default CreateSprintModal;