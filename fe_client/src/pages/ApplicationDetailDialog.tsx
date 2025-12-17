// src/pages/ApplicationDetailDialog.tsx
import { useRef, useEffect, useState, useMemo } from "react";
import { FileText, ExternalLink, Clock, CheckCircle2, XCircle, Trash2, X } from "lucide-react";
import { Application } from "./status-page";
import { Button } from "../components/ui/button";
import { withdrawApplication } from "../api/applicationApi";
import { toast } from "sonner";

interface ApplicationDetailDialogProps {
  isOpen: boolean;
  onClose: () => void;
  application: Application | null;
  onApplicationUpdate: () => void;
}

// Helper function để lấy config hiển thị trạng thái (đưa ra ngoài component để tránh khởi tạo lại)
const getStatusConfig = (status?: Application["status"]) => {
  switch (status) {
    case "SUBMITTED":
      return { label: "Đã nộp", icon: Clock, color: "text-yellow-600", bgColor: "bg-yellow-50", borderColor: "border-yellow-200" };
    case "UNDER_REVIEW":
      return { label: "Đang xem xét", icon: FileText, color: "text-blue-600", bgColor: "bg-blue-50", borderColor: "border-blue-200" };
    case "APPROVED":
      return { label: "Đã duyệt", icon: CheckCircle2, color: "text-green-600", bgColor: "bg-green-50", borderColor: "border-green-200" };
    case "CONFIRM":
      return { label: "Đã xác nhận", icon: CheckCircle2, color: "text-emerald-600", bgColor: "bg-emerald-50", borderColor: "border-emerald-200" };
    case "REJECTED":
      return { label: "Bị từ chối", icon: XCircle, color: "text-red-600", bgColor: "bg-red-50", borderColor: "border-red-200" };
    case "WITHDRAWN":
      return { label: "Rút lại", icon: XCircle, color: "text-red-600", bgColor: "bg-red-50", borderColor: "border-red-200" };
    default:
      return { label: "Không xác định", icon: Clock, color: "text-gray-600", bgColor: "bg-gray-50", borderColor: "border-gray-200" };
  }
};

export default function ApplicationDetailDialog({ isOpen, onClose, application, onApplicationUpdate }: ApplicationDetailDialogProps) {
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const mainDialogRef = useRef<HTMLDivElement | null>(null);

  // 1. Chuẩn hóa dữ liệu để tránh dùng "as any" quá nhiều
  const normalizedData = useMemo(() => {
    if (!application) return null;
    const raw = application as any; // Ép kiểu 1 lần duy nhất ở đây
    return {
      ...application,
      // Xử lý các trường có thể bị typo từ Backend hoặc null
      createdAt: raw.createdAt || raw.createAt,
      updatedAt: raw.updatedAt,
      cvUrl: raw.cvUrl || raw.CVUrl,
      internshipApplicationUrl: raw.internshipApplicationUrl || raw.internshipApplicationtUrl,
      internshipContractUrl: raw.internshipContractUrl || raw.internshipContracttUrl,
    };
  }, [application]);

  // Handle Click Outside cho Main Dialog
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      // Nếu đang hiện confirm dialog thì không đóng main dialog
      if (showConfirmDialog) return;
      
      if (mainDialogRef.current && !mainDialogRef.current.contains(event.target as Node)) {
        onClose();
      }
    };
    if (isOpen) document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [isOpen, onClose, showConfirmDialog]);

  if (!isOpen || !normalizedData) return null;

  const statusConfig = getStatusConfig(normalizedData.status);
  const StatusIcon = statusConfig.icon;

  const handleWithdraw = async () => {
    try {
      await withdrawApplication(normalizedData.id);
      toast.success('Rút đơn ứng tuyển thành công!');
      onApplicationUpdate();
      onClose();
    } catch (error) {
      console.error("Lỗi khi rút đơn:", error);
      toast.error("Đã có lỗi xảy ra khi rút đơn.");
    } finally {
      setShowConfirmDialog(false);
    }
  };

  const formatDate = (d?: string) => {
    if (!d) return "-";
    return String(d).split(" ")[0]; // Hoặc dùng moment/date-fns: format(new Date(d), 'dd/MM/yyyy')
  };

  const openDocument = (url?: string) => {
    if (url) window.open(url, "_blank");
  };

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/40 backdrop-blur-sm animate-fadeIn p-4">
      <div 
        ref={mainDialogRef}
        className="max-w-xl max-h-[85vh] bg-white rounded-xl shadow-2xl w-full flex flex-col transform scale-100 animate-slideUp"
      >
        {/* Header - Fixed */}
        <div className="flex items-center gap-3 border-b p-4">
          <div className="bg-blue-100 text-blue-600 p-2 rounded-full flex-shrink-0">
            <FileText className="w-6 h-6" />
          </div>
          <div className="flex-1">
            <h2 className="text-xl font-bold text-slate-800">CHI TIẾT ĐƠN ỨNG TUYỂN</h2>
            <p className="text-sm text-slate-600">Thông tin hồ sơ sinh viên</p>
          </div>
          <button onClick={onClose} className="cursor-pointer p-2 hover:bg-slate-100 rounded-full transition-colors">
            <X className="w-5 h-5 text-slate-500" />
          </button>
        </div>

        {/* Body - Scrollable */}
        <div className="overflow-y-auto p-4 sm:p-6 space-y-6 break-words">
          {/* Info Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm sm:text-base">
            <div>
                <label className="block text-sm text-slate-500 mb-1">Mã đơn</label>
                <div className="font-medium text-slate-900">#{normalizedData.id}</div>
            </div>
            <div>
              <label className="block text-sm text-slate-500 mb-1">Trạng thái</label>
              <div className={`inline-flex items-center gap-2 px-3 py-1 ${statusConfig.bgColor} border ${statusConfig.borderColor} rounded-full text-xs font-medium`}>
                <StatusIcon className={`w-4 h-4 ${statusConfig.color}`} />
                <span className={statusConfig.color}>{statusConfig.label}</span>
              </div>
            </div>
            
            <div className="sm:col-span-2">
                <label className="block text-sm text-slate-500 mb-1">Trường đại học</label>
                <div className="font-medium text-slate-900">{normalizedData.university}</div>
            </div>
            <div>
                <label className="block text-sm text-slate-500 mb-1">Chuyên ngành</label>
                <div className="font-medium text-slate-900">{normalizedData.major}</div>
            </div>
            <div>
                <label className="block text-sm text-slate-500 mb-1">Chương trình</label>
                <div className="font-medium text-slate-900">{normalizedData.internshipProgram}</div>
            </div>

            <div>
              <label className="block text-sm text-slate-500 mb-1">Ngày nộp</label>
              <div className="font-medium text-slate-900">{formatDate(normalizedData.createdAt)}</div>
            </div>
            <div>
              <label className="block text-sm text-slate-500 mb-1">Cập nhật cuối</label>
              <div className="font-medium text-slate-900">{formatDate(normalizedData.updatedAt)}</div>
            </div>
          </div>

          {/* Documents Section */}
          <div className="border-t border-slate-200 pt-4 space-y-3">
            <h4 className="font-semibold text-slate-900 flex items-center gap-2">
               Tài liệu đính kèm
            </h4>
            
            <DocumentItem 
                label="CV Cá nhân" 
                url={normalizedData.cvUrl} 
                btnText="Xem CV" 
                onClick={() => openDocument(normalizedData.cvUrl)}
            />
             <DocumentItem 
                label="Đơn xin thực tập" 
                url={normalizedData.internshipApplicationUrl} 
                btnText="Xem Đơn" 
                onClick={() => openDocument(normalizedData.internshipApplicationUrl)}
            />
             <DocumentItem 
                label="Hợp đồng thực tập" 
                url={normalizedData.internshipContractUrl} 
                btnText="Xem Hợp đồng" 
                onClick={() => openDocument(normalizedData.internshipContractUrl)}
            />
          </div>
        </div>

        {/* Footer - Fixed */}
        <div className="border-t border-slate-200 p-4 bg-slate-50 rounded-b-xl flex flex-col-reverse sm:flex-row justify-between gap-3">
            <Button
              variant="outline"
              onClick={onClose}
              className="cursor-pointer h-10 text-slate-600 border-slate-300 hover:bg-slate-100"
            >
              Đóng
            </Button>

            {(normalizedData.status === 'SUBMITTED' || normalizedData.status === 'UNDER_REVIEW') && (
               <Button
                variant="destructive" // Sử dụng variant destructive của shadcn nếu có
                onClick={() => setShowConfirmDialog(true)}
                className="cursor-pointer h-10 bg-red-600 hover:bg-red-700 text-white gap-2"
              >
                <Trash2 className="w-4 h-4" />
                Rút đơn ứng tuyển
              </Button>
            )}
        </div>
      </div>

      {/* CONFIRM DIALOG - Tách riêng để tránh lỗi layout lồng nhau */}
      {showConfirmDialog && (
        <div className="fixed inset-0 z-[60] flex items-center justify-center bg-black/50 backdrop-blur-sm p-4 animate-fadeIn">
          <div className="bg-white p-6 rounded-xl shadow-xl max-w-sm w-full animate-scaleIn">
            <div className="flex flex-col items-center text-center">
                <div className="bg-red-100 p-3 rounded-full mb-4">
                    <Trash2 className="w-8 h-8 text-red-600" />
                </div>
                <h3 className="text-xl font-bold mb-2 text-slate-900">Xác nhận rút đơn</h3>
                <p className="text-slate-600 mb-6">
                  Bạn có chắc chắn muốn rút đơn ứng tuyển này không? <br/>
                  <span className="text-red-500 text-sm">Hành động này không thể hoàn tác.</span>
                </p>
                
                <div className="flex gap-3 w-full">
                  <Button
                    variant="outline"
                    className="flex-1"
                    onClick={() => setShowConfirmDialog(false)}
                  >
                    Hủy
                  </Button>
                  <Button
                    className="flex-1 bg-red-600 hover:bg-red-700 text-white"
                    onClick={handleWithdraw}
                  >
                    Xác nhận rút
                  </Button>
                </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

// Component con để render item tài liệu cho gọn code
const DocumentItem = ({ label, url, btnText, onClick }: { label: string, url: string | null, btnText: string, onClick: () => void }) => (
    <div className="p-3 bg-slate-50 rounded-lg border border-slate-200 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
        <div className="flex items-center gap-2 text-slate-700">
            <FileText className="w-4 h-4 text-slate-400" />
            <span className="font-medium text-sm">{label}</span>
        </div>
        {url ? (
            <Button
                onClick={onClick}
                size="sm"
                className="cursor-pointer bg-white hover:bg-blue-50 text-blue-600 border border-blue-200 shadow-sm h-8 px-3 text-xs"
            >
                {btnText} <ExternalLink className="w-3 h-3 ml-1.5" />
            </Button>
        ) : (
            <span className="text-xs text-slate-400 italic">Chưa cập nhật</span>
        )}
    </div>
);