import React from "react";
import Modal from "./Modal";

const ViewEvaluationModal = ({ evaluation, onClose, onEdit }) => {
  if (!evaluation) return null;

  const { intern, averageScore, assessment } = evaluation;

  const formatSoftSkill = (skill) => {
    switch (skill) {
      case "GOOD":
        return "Tốt";
      case "FAIR":
        return "Khá";
      case "AVERAGE":
        return "Trung bình";
      case "POOR":
        return "Kém";
      default:
        return "N/A";
    }
  };

  const getSoftSkillColor = (skill) => {
    switch (skill) {
      case "GOOD":
        return "#28a745";
      case "FAIR":
        return "#007bff";
      case "AVERAGE":
        return "#ffc107";
      case "POOR":
        return "#dc3545";
      default:
        return "#6c757d";
    }
  };

  return (
    <Modal title={`Xem Đánh Giá: ${intern.name}`} onClose={onClose}>
      <div
        className="evaluation-modal-content"
        style={{
          padding: '24px',
          width: '100%',
          fontFamily: 'Roboto, Arial, sans-serif',
        }}
      >
        <div
          className="section"
          style={{ marginBottom: '18px' }}
        >
          <h3 style={{ marginTop: 0, marginBottom: '14px', fontSize: '20px', color: '#333', fontWeight: '600', letterSpacing: '0.5px', lineHeight: '1.4' }}>Kỹ năng chuyên môn</h3>
          <p style={{ marginBottom: '18px', fontSize: '16px', color: '#555', lineHeight: '1.6', letterSpacing: '0.3px' }}>
            Hiểu biết chuyên môn:{" "}
            <span
              className="score"
              style={{ fontWeight: 'bold', color: '#28a745' }}
            >
              {evaluation.expertiseScore}/10
            </span>
          </p>
          <p style={{ marginBottom: '18px', fontSize: '16px', color: '#555', lineHeight: '1.6', letterSpacing: '0.3px' }}>
            Chất lượng công việc / độ chính xác:{" "}
            <span
              className="score"
              style={{ fontWeight: 'bold', color: '#28a745' }}
            >
              {evaluation.qualityScore}/10
            </span>
          </p>
          <p style={{ marginBottom: '18px', fontSize: '16px', color: '#555', lineHeight: '1.6', letterSpacing: '0.3px' }}>
            Tư duy giải quyết vấn đề:{" "}
            <span
              className="score"
              style={{ fontWeight: 'bold', color: '#28a745' }}
            >
              {evaluation.problemSolvingScore}/10
            </span>
          </p>
          <p style={{ marginBottom: '18px', fontSize: '16px', color: '#555', lineHeight: '1.6', letterSpacing: '0.3px' }}>
            Khả năng học hỏi công nghệ mới:{" "}
            <span
              className="score"
              style={{ fontWeight: 'bold', color: '#28a745' }}
            >
              {evaluation.technologyLearningScore}/10
            </span>
          </p>
          <div
            className="average-score-display"
            style={{ marginTop: '18px', fontWeight: 'bold', fontSize: '18px', color: '#333', lineHeight: '1.5', letterSpacing: '0.4px' }}
          >
            Điểm trung bình: <span>{averageScore}/10</span>
          </div>
        </div>

        <div
          className="section"
          style={{ marginBottom: '18px' }}
        >
          <p style={{ marginTop: 0, marginBottom: '16px', fontSize: '18px', fontWeight: 'bold', color: '#333', lineHeight: '1.5', letterSpacing: '0.4px' }}>
            Thái độ & Kỹ năng mềm:{" "}
            <span
              className="score"
              style={{ fontWeight: '600', color: getSoftSkillColor(evaluation.softSkill) }}
            >
              {formatSoftSkill(evaluation.softSkill)}
            </span>
          </p>
        </div>

        <div
          className="section"
          style={{ marginBottom: '18px' }}
        >
          <h3 style={{ marginTop: 0, marginBottom: '18px', fontSize: '20px', color: '#333', fontWeight: '600', letterSpacing: '0.5px', lineHeight: '1.4' }}>Nhận xét</h3>
          <textarea
            value={assessment}
            readOnly
            rows="4"         
            style={{
              width: '100%',
              padding: '10px',
              border: '1px solid #ccc',
              borderRadius: '6px',
              resize: 'vertical',
              minHeight: '80px',
              fontSize: '15px',
              background: '#f9f9f9',
              color: '#333',
              lineHeight: '1.6',
              fontFamily: 'Roboto, Arial, sans-serif',
            }}
          />
        </div>

        <div
          className="modal-actions"
          style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            gap: '12px',
            marginTop: '24px'
          }}
        >
          <button
            type="button"           
            onClick={onClose}
            style={{
              padding: '10px 20px',
              border: '1px solid #d1d5db',
              borderRadius: '8px',
              cursor: 'pointer',
              fontWeight: '600',
              background: 'white',
              color: '#374151',
              fontSize: '15px',
              letterSpacing: '0.3px',    
              marginLeft: '230px',
            }}
          >
            Đóng
          </button>

          <button
            type="button"    
            onClick={onEdit}
            style={{
              padding: '10px 20px',
              border: 'none',
              borderRadius: '8px',
              cursor: 'pointer',
              fontWeight: '600',
              background: 'blue',
              color: 'white',
              fontSize: '15px',
              letterSpacing: '0.3px'
            }}
          >
            Chỉnh sửa
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default ViewEvaluationModal;