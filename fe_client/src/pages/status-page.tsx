// src/pages/StatusPage.tsx
import { useEffect, useState } from "react";
import {  useOutletContext } from "react-router-dom";
import {
  Clock,
  CheckCircle2,
  XCircle,
  FileText,
  ArrowLeft,
  Ban,
} from "lucide-react";
import { Toaster } from "../components/ui/sonner";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { getMyApplication } from "../api/applicationApi";
import ApplicationDetailDialog from "./ApplicationDetailDialog";

interface AppContextType {
  isLoggedIn: boolean;
  onBack: () => void;
  onSwitchToContractPage: () => void;
  onRegisterClick: () => void;
  onContractClick: () => void;
  onStatusChange?: (newStatus: Application["status"]) => void;
  onDeleteApplication: () => void;
}

export interface Application {
  id: string;
  status:
  | "SUBMITTED"
  | "UNDER_REVIEW"
  | "APPROVED"
  | "CONFIRM"
  | "REJECTED"
  | "WITHDRAWN";
  university: string;
  major: string;
  applicantName: string;
  createAt: string;
  cvUrl: string;
  internshipApplicationtUrl: string;
  internshipContractUrl: string | null;
  internshipProgram: string;
}

const getStatusConfig = (status: Application["status"]) => {
  switch (status) {
    case "SUBMITTED":
      return {
        label: "Đã nộp",
        icon: Clock,
        color: "text-yellow-600",
        bgColor: "bg-yellow-50",
        borderColor: "border-yellow-200",
      };
    case "UNDER_REVIEW":
      return {
        label: "Đang xem xét",
        icon: FileText,
        color: "text-blue-600",
        bgColor: "bg-blue-50",
        borderColor: "border-blue-200",
      };
    case "APPROVED":
      return {
        label: "Đã duyệt",
        icon: CheckCircle2,
        color: "text-green-600",
        bgColor: "bg-green-50",
        borderColor: "border-green-200",
      };
    case "CONFIRM":
      return {
        label: "Đã xác nhận",
        icon: CheckCircle2,
        color: "text-emerald-600",
        bgColor: "bg-emerald-50",
        borderColor: "border-emerald-200",
      };
    case "REJECTED":
      return {
        label: "Bị từ chối",
        icon: XCircle,
        color: "text-red-600",
        bgColor: "bg-red-50",
        borderColor: "border-red-200",
      };
    case "WITHDRAWN":
      return {
        label: "Rút lại",
        icon: Ban,
        color: "text-red-600",
        bgColor: "bg-red-50",
        borderColor: "border-red-200",
      };
    default:
      return {
        label: "Không xác định",
        icon: Clock,
        color: "text-gray-600",
        bgColor: "bg-gray-50",
        borderColor: "border-gray-200",
      };
  }
};

export function StatusPage() {
  const {
    isLoggedIn,
    onBack,
    onRegisterClick,
    onSwitchToContractPage,
  } = useOutletContext<AppContextType>();

  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedApp, setSelectedApp] = useState<Application | null>(null);
  const [isDetailOpen, setIsDetailOpen] = useState(false);
  const navigate = useNavigate();

  const fetchApplications = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("Vui lòng đăng nhập lại");

      const data = await getMyApplication(token);
      if (Array.isArray(data)) {
        const mapped = data.map((d) => ({
          id: d.id.toString(),
          status: mapStatus(d.internshipApplicationStatus),
          university: d.universityName,
          major: d.majorName,
          applicantName: d.fullName,
          createAt: d.createdAt,
          cvUrl: d.cvUrl,
          internshipApplicationtUrl: d.internshipApplicationtUrl,
          internshipContractUrl: d.internshipContractUrl,
          internshipProgram: d.internshipProgram,
        }));
        setApplications(mapped);
      } else {
        setApplications([]);
      }
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "Lỗi khi tải danh sách ứng tuyển"
      );
    } finally {
      setLoading(false);
    }
  };

  const mapStatus = (backendStatus: string): Application["status"] => {
    switch (backendStatus) {
      case "UNDER_REVIEW":
        return "UNDER_REVIEW";
      case "APPROVED":
        return "APPROVED";
      case "CONFIRM":
        return "CONFIRM";
      case "REJECTED":
        return "REJECTED";
      case "WITHDRAWN":
        return "WITHDRAWN";
      default:
        return "SUBMITTED";
    }
  };

  useEffect(() => {
    fetchApplications();
  }, [isLoggedIn]);

  const handleOpenDetail = (app: Application) => {
    setSelectedApp(app);
    setIsDetailOpen(true);
  };

  const handleCloseDetail = () => {
    setSelectedApp(null);
    setIsDetailOpen(false);
  };

  if (loading)
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-slate-600">Đang tải...</p>
      </div>
    );

  if (error)
    return (
      <div className="min-h-screen flex flex-col items-center justify-center">
        <XCircle className="w-12 h-12 text-red-600 mb-4" />
        <p>{error}</p>
      </div>
    );

  if (!isLoggedIn)
    return (
      <div className="min-h-screen bg-white pt-20">
        <Toaster />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
          <button
            onClick={onBack}
            className="flex items-center gap-2 text-slate-600 hover:text-orange-600 transition-colors mb-8"
          >
            <ArrowLeft className="w-5 h-5" />
            Quay lại
          </button>

          <div className="max-w-md mx-auto">
            <div className="bg-white rounded-2xl shadow-xl p-8 border border-slate-200 text-center">
              <div className="w-20 h-20 bg-orange-100 rounded-full flex items-center justify-center mx-auto mb-6">
                <FileText className="w-10 h-10 text-orange-600" />
              </div>
              <h2 className="text-2xl text-slate-900 mb-4">
                Vui lòng đăng nhập
              </h2>
              <p className="text-slate-600 mb-6">
                Bạn cần đăng nhập để xem trạng thái đơn ứng tuyển của mình.
              </p>
              <button
                onClick={onRegisterClick}
                className="w-full px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-lg hover:shadow-orange-500/30 transition-all"
              >
                Đăng ký/Đăng nhập
              </button>
            </div>
          </div>
        </div>
      </div>
    );

  return (
    <div className="min-h-screen bg-white pt-20">
      <Toaster />
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <button
          onClick={onBack}
          className="flex items-center gap-2 text-slate-600 hover:text-orange-600 transition-colors mb-8"
        >
          <ArrowLeft className="w-5 h-5" /> Quay lại
        </button>

        <h1 className="text-4xl font-bold text-slate-900 text-center mb-8">
          Danh sách đơn ứng tuyển của bạn
        </h1>

        {applications.length === 0 ? (
          <div className="text-center py-16">
            <FileText className="w-12 h-12 text-slate-400 mx-auto mb-4" />
            <p className="text-slate-600">Bạn chưa nộp đơn ứng tuyển nào.</p>
          </div>
        ) : (
          <div className="space-y-6">
            {applications.map((app) => {
              const statusConfig = getStatusConfig(app.status);
              const StatusIcon = statusConfig.icon;

              return (
                <div
                  key={app.id}
                  className={`bg-white rounded-xl p-6 border-2 ${statusConfig.borderColor} shadow-md hover:shadow-xl transition`}
                >
                  <div className="flex flex-col md:flex-row md:items-start justify-between gap-6">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start gap-4 mb-4">
                        <div
                          className={`w-12 h-12 ${statusConfig.bgColor} rounded-xl flex items-center justify-center`}
                        >
                          <StatusIcon
                            className={`w-6 h-6 ${statusConfig.color}`}
                          />
                        </div>

                        <div className="flex-1 min-w-0">
                          <h4 className="text-xl font-semibold text-slate-900 mb-1">
                            {app.university}
                          </h4>
                          <p className="text-slate-600">
                            Chuyên ngành: {app.major}
                          </p>
                          <p className="flex items-center gap-2 text-sm text-slate-600">
                            Ngày nộp: {app.createAt}
                          </p>
                        </div>
                      </div>
                      <div className="space-y-2">
                        <p className="text-sm text-slate-700 font-medium">
                          Trạng thái hồ sơ:
                        </p>

                        <div className={`inline-flex items-center gap-2 px-4 py-2 ${statusConfig.bgColor} border ${statusConfig.borderColor} rounded-full`}>
                          <StatusIcon className={`w-4 h-4 ${statusConfig.color}`} />
                          <span className={`${statusConfig.color}`}>
                            {statusConfig.label}
                          </span>
                        </div>
                      </div>
                      </div>

                      <div className="flex flex-col md:items-end gap-3 w-full md:w-auto">
                        {(app.status === "APPROVED" ||
                          app.status === "CONFIRM") && (
                            <button
                              onClick={onSwitchToContractPage}
                              className="w-full md:w-auto px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-lg hover:shadow-orange-500/30 transition-all duration-300 flex items-center justify-center gap-2"
                            >
                              <FileText className="w-5 h-5" />
                              Hợp đồng thực tập
                            </button>
                          )}
                        <button
                          onClick={() => handleOpenDetail(app)}
                          className="w-full md:w-auto px-6 py-2 border-2 border-slate-300 text-slate-700 rounded-lg hover:bg-slate-50 transition-colors">
                          Xem chi tiết
                        </button>
                      </div>
                    </div>
                    {app.status === "SUBMITTED" && (
                      <p className="mt-6 text-sm text-slate-600 border-t pt-4">
                        📋 Hồ sơ của bạn đã được nộp và đang chờ xử lý. Chúng tôi
                        sẽ phản hồi trong vòng 3–5 ngày.
                      </p>
                    )}
                    {app.status === "UNDER_REVIEW" && (
                      <p className="mt-6 text-sm text-slate-600 border-t pt-4">
                        👀 Hồ sơ đang được xem xét kỹ lưỡng. Chúng tôi sẽ sớm gửi
                        thông báo kết quả.
                      </p>
                    )}
                    {app.status === "APPROVED" && (
                      <p className="mt-6 text-sm text-green-700 border-t pt-4 bg-green-50 rounded-lg p-4">
                        🎉 Hồ sơ của bạn đã được phê duyệt. Hãy xem và xác nhận hợp
                        đồng thực tập.
                      </p>
                    )}
                    {app.status === "CONFIRM" && (
                      <p className="mt-6 text-sm text-emerald-700 border-t pt-4 bg-emerald-50 rounded-lg p-4">
                        ✅ Bạn đã xác nhận hợp đồng thực tập thành công. Chúng tôi sẽ
                        liên hệ với bạn về ngày bắt đầu.
                      </p>
                    )}
                    {app.status === "REJECTED" && (
                      <p className="mt-6 text-sm text-slate-600 border-t pt-4">
                        ❌ Hồ sơ chưa phù hợp. Hãy tiếp tục rèn luyện và ứng tuyển
                        lại sau.
                      </p>
                    )}
                    {app.status === "WITHDRAWN" && (
                      <p className="mt-6 text-sm text-slate-600 border-t pt-4">
                        🚫 Hồ sơ của bạn đã được rút. Bạn có thể nộp lại nếu vẫn
                        quan tâm.
                      </p>
                    )}
                  </div>
                  );
            })}
                </div>
              )
            }
      </div>

      {/* Dialog xem chi tiết */}
        <ApplicationDetailDialog
          isOpen={isDetailOpen}
          onClose={handleCloseDetail}
          application={selectedApp}
          onApplicationUpdate={fetchApplications} // Truyền hàm fetchApplications vào
        />
      </div>
      );
}
