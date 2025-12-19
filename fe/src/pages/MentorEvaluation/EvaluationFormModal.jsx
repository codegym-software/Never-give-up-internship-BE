import React, { useMemo } from "react";
import Modal from "./Modal";

const criteria = [
  { key: "knowledge", label: "Hiểu biết chuyên môn" },
  { key: "quality", label: "Chất lượng công việc / độ chính xác" },
  { key: "problemSolving", label: "Tư duy giải quyết vấn đề" },
  { key: "learning", label: "Khả năng học hỏi công nghệ mới" },
];

const EvaluationFormModal = ({ intern, form, setForm, onSubmit, onClose }) => {

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    const [parent, child] = name.split(".");

    if (child) {
      let numericValue = parseFloat(value);

      if (type === 'range') {
        let finalValue = numericValue;
        if (finalValue < 1 || isNaN(finalValue)) {
          finalValue = 1;
        } else if (finalValue > 10) {
          finalValue = 10;
        }
        finalValue = Math.round(finalValue * 2) / 2;

        setForm((prev) => ({
          ...prev,
          [parent]: { ...prev[parent], [child]: finalValue },
        }));
      } else {
        setForm((prev) => ({
          ...prev,
          [parent]: { ...prev[parent], [child]: value },
        }));
      }
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    const [parent, child] = name.split(".");

    let numericValue = parseFloat(value);
    let finalValue;

    if (isNaN(numericValue) || numericValue < 1) {
      finalValue = 1;
      if (numericValue !== 0 && value !== "") {
        alert("Điểm tối thiểu là 1.");
      }
    } else if (numericValue > 10) {
      finalValue = 10;
      alert("Điểm tối đa là 10.");
    } else {
      finalValue = numericValue;
    }

    finalValue = Math.round(finalValue * 2) / 2;

    setForm((prev) => ({
      ...prev,
      [parent]: { ...prev[parent], [child]: finalValue },
    }));
  };

  const avgScore = useMemo(() => {
    const scores = Object.values(form.technical || {}).map(score => parseFloat(score) || 0);
    const validScores = scores.filter(score => score >= 1 && score <= 10);

    if (validScores.length === 0) return 0.0;

    const sum = validScores.reduce((a, b) => a + b, 0);
    return (sum / validScores.length).toFixed(1);

  }, [form.technical]);


  return (
    <Modal title={`${intern.evaluated ? "Chỉnh sửa đánh giá" : "Đánh giá"}: ${intern.name}`} onClose={onClose}>
      <form onSubmit={onSubmit}>
        <div className="form-section">
          <h3 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '10px', color: '#2c3e50' }}>Kỹ năng chuyên môn</h3>
          {criteria.map((c) => (
            <div className="form-group" key={c.key}>
              <label style={{ display: 'block', fontSize: '14px', fontWeight: '500', color: '#495057' }}>{c.label}</label>
              <div
                className="score-input-group"
                style={{ display: 'flex', alignItems: 'center', gap: '10px' }}
              >

                {/* 1. Thanh trượt */}
                <input
                  type="range"
                  name={`technical.${c.key}`}
                  min="1"
                  max="10"
                  step="0.5"         
                  value={parseFloat(form.technical[c.key]) || 1}
                  onChange={handleChange}
                  className="slider"
                  style={{ flexGrow: 1 }}
                />

                {/* 2. Ô điền số (UX mượt mà hơn) */}
                <input
                  type="number"
                  name={`technical.${c.key}`}
                  step="0.5"                 
                  value={form.technical[c.key]}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  className="score-number-input"
                  style={{ width: '70px', padding: '5px 17px', borderRadius: '6px', border: '1px solid #ccc' }}
                />

                {/* 3. Hiển thị điểm */}
                <span className="slider-value" style={{ width: '40px', textAlign: 'right' }}>
                  {parseFloat(form.technical[c.key]) || 1}/10
                </span>
              </div>
            </div>
          ))}
          <div className="avg-score" style={{ marginTop: '16px', marginBottom: '10px', fontSize: '16px', fontWeight: '600', color: '#28a745' }}>
            <strong>Điểm trung bình: {avgScore}</strong>
          </div>
        </div>

        <div className="form-section" >
          <h3 style={{ fontSize: '18px', fontWeight: '600', marginBottom: '10px', color: '#2c3e50' }}>Thái độ & Kỹ năng mềm</h3>
          <div className="radio-group">
            {["Tốt", "Khá", "Trung bình", "Kém"].map((level) => (
              <label key={level} style={{ fontSize: '14px', color: '#495057', padding: '2px 6px' }}>
                <input
                  type="radio"
                  name="softSkills"
                  value={level}
                  checked={form.softSkills === level}
                  onChange={handleChange}
                  style={{ marginRight: '4px', accentColor: '#007bff' }}
                />
                <span className="radio-text">{level}</span>
              </label>
            ))}
          </div>
        </div>

        <div className="form-section">
          <h3>Nhận xét</h3>
          <textarea
            name="assessment"
            value={form.assessment}
            onChange={handleChange}
            rows="5"
            placeholder="Ưu điểm: ... | Khuyết điểm: ..."
            style={{
              width: '100%',
              padding: '10px',
              border: '1px solid #ccc',
              borderRadius: '8px',
              resize: 'vertical',
              minHeight: '80px',
              fontSize: '15px', 
              background: '#f9f9f9',
              color: '#333',
              fontFamily: 'Roboto, Arial, sans-serif', 
            }}
            required
          />
        </div>

        <div className="form-actions" style={{ marginTop: '24px', display: 'flex', justifyContent: 'flex-end' }}>
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
              marginLeft: '285px',
            }}
          >
            Đóng
          </button>
          <button
            type="button"
            onClick={onSubmit}
            style={{
              padding: '10px 20px',
              border: 'none',
              borderRadius: '8px',
              cursor: 'pointer',
              fontWeight: '600',
              background: 'blue',
              color: 'white',
              fontSize: '15px',
              letterSpacing: '0.3px',
              marginLeft: '26px',
            }}
          >
            {intern.evaluated ? "Cập nhật" : "Gửi Đánh Giá"}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default EvaluationFormModal;