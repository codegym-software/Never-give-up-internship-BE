import React, { useState, useEffect } from "react";
import Modal from "~/pages/MentorEvaluation/Modal.jsx";
import { toast } from "react-toastify";
import "./AllowancePackageFormModal.css";

const AllowancePackageFormModal = ({
  isOpen,
  onClose,
  onSubmit,
  initialData,
  internshipPrograms,
}) => {
  const [formData, setFormData] = useState({
    name: "",
    amount: "",
    requiredWorkDays: "",
    internshipProgramId: "",
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        name: initialData.name || "",
        amount: initialData.amount || "",
        requiredWorkDays: initialData.requiredWorkDays || "",
        internshipProgramId: initialData.internshipProgram?.id || "",
      });
    } else {
      setFormData({
        name: "",
        amount: "",
        requiredWorkDays: "",
        internshipProgramId: "",
      });
    }
  }, [initialData, isOpen]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (
      !formData.name ||
      !formData.amount ||
      !formData.requiredWorkDays ||
      !formData.internshipProgramId
    ) {
      toast.error("Vui lòng điền đầy đủ các trường bắt buộc.");
      return;
    }
    onSubmit(formData);
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={initialData ? "Sửa Gói Phụ Cấp" : "Tạo Gói Phụ Cấp Mới"}
    >
      <form className="ap-form" onSubmit={handleSubmit}>
        <div className="ap-field">
          <label>Tên gói phụ cấp</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="ap-field">
          <label>Chương trình thực tập</label>
          <select
            name="internshipProgramId"
            value={formData.internshipProgramId}
            onChange={handleChange}
            required
          >
            <option value="">Chọn chương trình thực tập</option>
            {internshipPrograms.map((program) => (
              <option key={program.id} value={program.id}>
                {program.name}
              </option>
            ))}
          </select>
        </div>

        <div className="ap-field">
          <label>Số tiền (VND)</label>
          <input
            type="number"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            required
          />
        </div>

        <div className="ap-field">
          <label>Số ngày làm việc tối thiểu</label>
          <input
            type="number"
            name="requiredWorkDays"
            value={formData.requiredWorkDays}
            onChange={handleChange}
            required
          />
        </div>

        <div className="ap-actions">
          <button type="button" className="btn-cancell" onClick={onClose}>
            Hủy
          </button>
          <button type="submit" className="btn-submit">
            {initialData ? "Cập nhật" : "Tạo mới"}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default AllowancePackageFormModal;
