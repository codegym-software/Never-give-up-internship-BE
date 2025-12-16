import { X } from "lucide-react";

const Detail = ({ details, onClose }) => {
  const translateStatus = (status) => {
    switch (status) {
      case "CHECKED_IN":
        return "Đã check-in";
      case "PRESENT":
        return "Có mặt";
      case "TIME_VIOLATION":
        return "Đi muộn/về sớm";
      case "EXCUSED_TIME":
        return "Đi muộn/về sớm có phép";
      case "ON_LEAVE":
        return "Nghỉ có phép";
      case "ABSENT":
        return "Vắng mặt";
      default:
        return status;
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-header">
          <h3>
            Chi tiết ngày {new Date(details.date).toLocaleDateString("vi-VN")}
          </h3>
          <button className="modal-close" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="detail-container">
          <div className="detail-row">
            <span className="label">Check-in:</span>
            <span className="value">{details.checkIn || "--"}</span>
          </div>
          <div className="detail-row">
            <span className="label">Check-out:</span>
            <span className="value">{details.checkOut || "--"}</span>
          </div>
          <div className="detail-row">
            <span className="label">Trạng thái:</span>
            <span className="value">
              {translateStatus(details.status) || "--"}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Detail;
