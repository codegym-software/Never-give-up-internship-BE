"use client";

import { useState, useEffect } from "react";
import { ArrowLeft, FileText, Download, Upload, CheckCircle2, Eye, X, Loader2, AlertTriangle } from "lucide-react";
import { Button } from "../components/ui/button";
import { Label } from "../components/ui/label";
import { useOutletContext } from "react-router-dom";
import type { AppContextType } from "../Root";
import { toast } from "sonner";
import ConfirmDialogExample from "./ConfirmDialogExample";
import { getMyApplication, submitContract } from "../api/applicationApi";
import { useAuth } from "../context/AuthContext";


// Backend returns more fields, but we only need these for now
interface Application {
  id: number; // API trả về id là number
  internshipApplicationStatus: "SUBMITTED" | "APPROVED" | "CONFIRM" | "REJECTED" | "WITHDRAWN";
  internshipContractUrl: string | null; // <-- SỬ DỤNG TÊN CHÍNH XÁC TỪ API
  // add other fields if needed
}

export function ContractPage() {
  const { onBack } = useOutletContext<AppContextType>();
  const { user } = useAuth(); // Get user from AuthContext

  const [applications, setApplications] = useState<Application[] | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [uploadedFile, setUploadedFile] = useState<File | null>(null);
  const [fileName, setFileName] = useState<string | null>(null);

  // TÁCH LOGIC GỌI API RA MỘT HÀM RIÊNG
  const fetchApplications = async () => {
    setIsLoading(true);
    try {
      const token = localStorage.getItem("accessToken")?.toString() || "";
      const myApplications = await getMyApplication(token);
      console.log("Fetched applications:", myApplications);
      // API trả về mảng các object, chúng ta cần map lại tên trường nếu cần
      // Dựa trên response của bạn, tên trường đã khớp với interface mới
      setApplications(myApplications || []);
    } catch (error) {
      console.error("Failed to fetch applications", error);
      toast.error("Không thể tải dữ liệu đơn ứng tuyển.");
      setApplications([]);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchApplications(); // Gọi lần đầu khi component mount
  }, []);

  const handleDownloadTemplate = () => {
    toast("Đang tải xuống hợp đồng mẫu từ Google Docs...");

    // 🔹 ID của file Google Docs (lấy từ đường dẫn tài liệu)
    // Ví dụ: https://docs.google.com/document/d/1AbCdEfGhIjKlMnOpQrStUvWxYz/edit
    // --> FILE_ID = 1AbCdEfGhIjKlMnOpQrStUvWxYz
    const googleDocsFileId = "1fqG4spzN7RBQxRzpktjQ_6ujXXoFwfgt8XI9aK_WVCI"; // ⚠️ Thay bằng ID thật

    // 🔹 Chọn định dạng cần tải (pdf hoặc docx)
    const downloadUrl = `https://docs.google.com/document/d/${googleDocsFileId}/export?format=pdf`;

    // 🔹 Mở link để tải
    window.open(downloadUrl, "_blank");
  };

  // Find the application that is approved, or already confirmed
  const relevantApplication = applications?.find(
    app => app.internshipApplicationStatus === "APPROVED" || app.internshipApplicationStatus === "CONFIRM"
  );

  const isConfirmed = relevantApplication?.internshipApplicationStatus === "CONFIRM";
  const isSubmittable = !!relevantApplication && !isConfirmed;


  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];

      if (file.size > 5 * 1024 * 1024) {
        toast.error("File quá lớn! Kích thước tối đa là 5MB.");
        return;
      }

      if (file.type !== "application/pdf") {
        toast.error("Vui lòng tải lên file PDF!");
        return;
      }

      setUploadedFile(file);
      setFileName(file.name);
    }
  };

  const confirmContractAction = async () => {
    if (!uploadedFile) {
      toast.warning("Vui lòng tải lên file hợp đồng trước khi xác nhận!");
      return;
    }
    if (!isSubmittable) {
      toast.error("Không thể xác nhận hợp đồng ở trạng thái này.");
      return;
    }

    setIsSubmitting(true);
    try {
      // API này chỉ upload file và trả về success message
      await submitContract(uploadedFile);
      toast.success("Xác nhận và gửi hợp đồng thành công!");

      // SAU KHI THÀNH CÔNG, GỌI LẠI HÀM FETCH ĐỂ LẤY DỮ LIỆU MỚI NHẤT
      await fetchApplications();

    } catch (error: any) {
      console.error("Failed to submit contract", error);
      const errorMessage = error?.response?.data?.message || "Gửi hợp đồng thất bại. Vui lòng thử lại.";
      toast.error(errorMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleViewContract = () => {
    // Ưu tiên 1: Mở link hợp đồng từ server nếu có
    if (relevantApplication?.internshipContractUrl) {
      window.open(relevantApplication.internshipContractUrl, '_blank');
      return;
    }
    // Ưu tiên 2: Mở file tạm thời người dùng vừa chọn
    if (uploadedFile) {
      const fileURL = URL.createObjectURL(uploadedFile);
      window.open(fileURL, '_blank');
      return;
    }
    // Trường hợp không có file nào để xem
    toast.warning("Chưa có file hợp đồng để xem!");
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-[60vh]">
        <Loader2 className="w-12 h-12 animate-spin text-orange-600" />
        <p className="ml-4 text-lg text-slate-700">Đang tải dữ liệu...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 py-12">
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <button
            onClick={onBack}
            className="flex items-center gap-2 text-slate-600 hover:text-orange-600 transition-colors mb-4"
          >
            <ArrowLeft className="w-5 h-5" />
            Quay lại
          </button>
          <h1 className="text-4xl text-slate-900 mb-2 text-center">HỢP ĐỒNG THỰC TẬP</h1>
          <p className="text-lg text-slate-600 text-center">
            Quản lý và xác nhận hợp đồng thực tập sinh
          </p>
        </div>

        {!relevantApplication && !isLoading && (
           <div className="bg-yellow-50 border-2 border-yellow-200 rounded-xl p-6 mb-6 text-center">
             <div className="flex items-center justify-center gap-3 text-yellow-800">
               <AlertTriangle className="w-6 h-6" />
               <div>
                 <h3 className="text-lg font-semibold">Không tìm thấy đơn ứng tuyển hợp lệ</h3>
                 <p className="text-sm mt-1">
                   Bạn chỉ có thể nộp hợp đồng sau khi đơn ứng tuyển của bạn được duyệt (Trạng thái: APPROVED).
                 </p>
               </div>
             </div>
           </div>
        )}

        {isConfirmed && (
          <div className="bg-green-50 border-2 border-green-200 rounded-xl p-6 mb-6">
            <div className="flex items-center gap-3 text-green-700 ">
              <CheckCircle2 className="w-6 h-6" />
              <div >
                <h3 className="text-lg font-semibold">Hợp đồng đã được xác nhận</h3>
                <p className="text-sm mt-1">Bạn không thể thay đổi hợp đồng sau khi đã xác nhận.</p>
              </div>
            </div>
          </div>
        )}

        {/* Contract Information Form */}
        <div className={`bg-white rounded-xl shadow-lg border-2 border-dashed border-slate-300 p-8 mb-6 max-w-3xl mx-auto ${!relevantApplication && 'opacity-50 pointer-events-none'}`}>
           <div className="flex items-center gap-3 mb-6 pb-4 border-b border-slate-200">
            <FileText className="w-6 h-6 text-slate-600" />
            <h2 className="text-2xl text-slate-900 text-center">THÔNG TIN HỢP ĐỒNG</h2>
          </div>
           <div className="space-y-6">
             <div className="grid grid-cols-[200px_1fr] gap-4 items-center">
              <Label className="text-slate-700">Họ và tên:</Label>
              <div className="text-slate-900 bg-slate-50 px-4 py-2 rounded border border-slate-200">
                {user?.fullName || "Chưa cập nhật"}
              </div>
            </div>
             <div className="grid grid-cols-[200px_1fr] gap-4 items-center">
              <Label className="text-slate-700">Trạng thái đơn:</Label>
               <div className={`px-4 py-2 rounded border inline-flex items-center gap-2 w-fit font-semibold ${
                isConfirmed ? "bg-green-50 border-green-200 text-green-700" : "bg-blue-50 border-blue-200 text-blue-700"
              }`}>
                <CheckCircle2 className="w-4 h-4" />
                 {relevantApplication?.internshipApplicationStatus || "N/A"}
              </div>
            </div>
          </div>
        </div>


        {/* Files & Actions Section - Allow interaction if a contract URL exists */}
        <div className={`bg-white rounded-xl shadow-lg border border-slate-200 p-8 mb-6 max-w-3xl mx-auto ${!isSubmittable && !relevantApplication?.internshipContractUrl && 'opacity-50 pointer-events-none'}`}>
           <div className="flex items-center gap-3 mb-6 pb-4 border-b border-slate-200">
            <FileText className="w-6 h-6 text-orange-600" />
            <h2 className="text-2xl text-slate-900">Tệp hợp đồng</h2>
          </div>

          <div className="grid md:grid-cols-2 gap-6">
            {/* Download Template */}
            <div className="border-2 border-dashed border-slate-300 rounded-lg p-6">
              <h3 className="text-lg text-slate-900 mb-3">📄 Hợp đồng mẫu</h3>
              <p className="text-sm text-slate-600 mb-4">
                Tải xuống hợp đồng mẫu, điền thông tin và tải lên.
              </p>
              <Button
                  type="button"
                  onClick={handleDownloadTemplate}
                  className="w-full bg-blue-500 hover:bg-blue-600 text-white"
                >
                  <Download className="w-4 h-4 mr-2" />
                  Tải về hợp đồng mẫu
                </Button>
            </div>

            {/* Upload Contract */}
            <div className="border-2 border-dashed border-slate-300 rounded-lg p-6 hover:border-orange-400 transition-colors">
              <h3 className="text-lg text-slate-900 mb-3">📤 Upload hợp đồng</h3>
              <p className="text-sm text-slate-600 mb-4">
                {fileName || "Tải lên file hợp đồng đã ký (PDF, tối đa 5MB)"}
              </p>
              <label className="block">
                <input
                  type="file"
                  accept=".pdf"
                  onChange={handleFileUpload}
                  className="hidden"
                  disabled={!isSubmittable || isSubmitting}
                />
                <Button
                  type="button"
                  onClick={() => (document.querySelector('input[type="file"]') as HTMLInputElement)?.click()}
                  className="w-full bg-orange-600 hover:bg-orange-700 text-white"
                  disabled={!isSubmittable || isSubmitting}
                >
                  <Upload className="w-4 h-4 mr-2" />
                  {fileName ? "Đổi file khác" : "Chọn file"}
                </Button>
              </label>
            </div>
          </div>

           <div className="flex flex-wrap gap-4 mt-6">
            <Button
              onClick={handleViewContract}
              disabled={!uploadedFile && !relevantApplication?.internshipContractUrl}
              className="flex-1 bg-slate-700 hover:bg-slate-800 text-white disabled:opacity-50"
            >
              <Eye className="w-4 h-4 mr-2" />
              Xem hợp đồng
            </Button>
          </div>
        </div>

        {/* Confirm Contract Button */}
        {isSubmittable && (
          <div className="bg-white rounded-xl shadow-lg border border-slate-200 p-6 mb-6 max-w-3xl mx-auto">
            <ConfirmDialogExample
              onConfirm={confirmContractAction}
              disabled={!uploadedFile || isSubmitting}
              isLoading={isSubmitting}
            />
            {!uploadedFile && (
              <p className="text-sm text-slate-600 mt-3 text-center">
                *Bạn chỉ có thể xác nhận sau khi file hợp đồng được upload.
              </p>
            )}
          </div>
        )}
      </div>
    </div>
  );
}