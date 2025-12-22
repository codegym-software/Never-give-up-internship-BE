import React, { useState } from "react";
import { Pencil, XCircle, Send, CheckCircle2 } from "lucide-react";
import EditInternshipProgramModal from "./EditInternshipProgramForm";
import Swal from "sweetalert2";
import {
  publishInternshipProgram,
  cancelInternshipProgram,
  completeInternshipProgram,
} from "~/services/InternshipProgramService";

const InternshipProgramTable = ({
    setInternshipPrograms,
  internshipPrograms,
  loading,
  convertToISO,
}) => {
  const [internshipProgram, setInternshipProgram] = useState(null);
  const [showEditForm, setShowEditForm] = useState(false);

  const publish = async (id) => {
    const result = await Swal.fire({
      title: "Xác nhận xuất bản kì thực tập",
      html: `
            <div style="text-align: center; padding: 10px 0;">
              <p>Bạn có chắc muốn xuất bản kì thực tập?</p>
            </div>
          `,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "blue",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Xuất bản",
      cancelButtonText: "Hủy bỏ",
      reverseButtons: true,
      customClass: {
        popup: "swal-wide",
      },
    });

    if (!result.isConfirmed) return;

    const data = await publishInternshipProgram(id);
    if (data) {
      setInternshipPrograms((prev) =>
        prev.map((internshipProgram) =>
          internshipProgram.id === data.id ? data : internshipProgram
        )
      );
    }
  };

  const cancel = async (id) => {
    const result = await Swal.fire({
      title: "Xác nhận hủy kì thực tập",
      html: `
            <div style="text-align: center; padding: 10px 0;">
              <p>Bạn có chắc muốn hủy kì thực tập?</p>
            </div>
          `,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "red",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Hủy kì thực tập",
      cancelButtonText: "Hủy bỏ",
      reverseButtons: true,
      customClass: {
        popup: "swal-wide",
      },
    });

    if (!result.isConfirmed) return;

    const data = await cancelInternshipProgram(id);
    if (data) {
      setInternshipPrograms((prev) =>
        prev.map((internshipProgram) =>
          internshipProgram.id === data.id ? data : internshipProgram
        )
      );
    }
  };

  const complete = async (id) => {
    const result = await Swal.fire({
      title: "Xác nhận kết thúc kì thực tập",
      html: `
            <div style="text-align: center; padding: 10px 0;">
              <p>Bạn có chắc muốn kết thúc kì thực tập?</p>
            </div>
          `,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "green",
      cancelButtonColor: "#6c757d",
      confirmButtonText: "Kết thúc",
      cancelButtonText: "Hủy bỏ",
      reverseButtons: true,
      customClass: {
        popup: "swal-wide",
      },
    });

    if (!result.isConfirmed) return;

    const data = await completeInternshipProgram(id);
    if (data) {
      setInternshipPrograms((prev) =>
        prev.map((internshipProgram) =>
          internshipProgram.id === data.id ? data : internshipProgram
        )
      );
    }
  };


  return (
    <>
      <div className="table-container">
        <table className="intern-table">
          <thead>
            <tr>
              <th>Kỳ thực tập</th>
              <th>Phòng ban</th>
              <th>Hạn nộp hồ sơ</th>
              <th>Hạn duyệt hồ sơ</th>
              <th>Bắt đầu</th>
              <th>Kết thúc</th>
              <th>Trạng thái</th>
              <th className="action-col"></th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
<td colSpan="9" className="text-center">
                  Đang tải...
                </td>
                              </tr>
            ) : internshipPrograms.length === 0 ? (
              <tr>
 <td colSpan="9" className="text-center">
                  Không có dữ liệu
                </td>
              </tr>
            ) : (
              internshipPrograms.map((ip) => (
                <tr key={ip.id}>
                  <td>{ip.name}</td>
                  <td>{ip.department}</td>
                  <td>{ip.endPublishedTime}</td>
                  <td>{ip.endReviewingTime}</td>
                  <td>{ip.timeStart}</td>
                  <td>{ip.timeEnd || "-"}</td>
                  <td>{ip.status}</td>
                  <td className="action-col">
                    <button
                      className="icon-btn"
                      onClick={() => {
                        setInternshipProgram(ip);
                        setShowEditForm(true);
                      }}
                    >
                      <Pencil size={15} />
                    </button>

                     {ip.status !== "CANCELLED" && ip.status !== "COMPLETED" && (
                      <>
                        {ip.status === "DRAFT" && (
                          <button
                            className="icon-btn"
                            onClick={() => publish(ip.id)}
                          >
                            <Send size={15} />
                          </button>
                        )}

                       {ip.status === "ONGOING" ? (
                          <button className="icon-btn">
                            <CheckCircle2
                              size={15}
                              onClick={() => complete(ip.id)}
                            />
                          </button>
                        ) : (
                          <button
                            className="icon-btn"
                            onClick={() => cancel(ip.id)}
                          >
                            <XCircle size={15} />
                          </button>
                        )}
                      </>
                    )}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {showEditForm && internshipProgram && (
        <EditInternshipProgramModal
                  setInternshipPrograms={setInternshipPrograms}
          onClose={() => setShowEditForm(false)}
          internshipProgram={internshipProgram}
          convertToISO={convertToISO}
        />
      )}
    </>
  );
};

export default InternshipProgramTable;