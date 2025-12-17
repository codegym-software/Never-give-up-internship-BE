
import { XCircle, ArrowLeft } from "lucide-react";
import { Link, useSearchParams } from "react-router-dom";

export function VerificationFailPage() {
  const [searchParams] = useSearchParams();
  const reason = searchParams.get("reason");

  const getErrorMessage = () => {
    switch (reason) {
      case "not_found":
        return "Mã xác thực không hợp lệ hoặc không tồn tại.";
      case "expired":
        return "Mã xác thực đã hết hạn. Vui lòng thử lại.";
      case "failed":
      default:
        return "Xác thực thất bại. Vui lòng liên hệ hỗ trợ.";
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 p-4">
      <div className="max-w-md w-full bg-white shadow-lg rounded-2xl p-8 border-t-4 border-red-500">
        <div className="text-center">
          <XCircle className="w-16 h-16 text-red-500 mx-auto mb-4" />
          <h1 className="text-3xl font-bold text-gray-800 mb-2">
            Xác thực thất bại!
          </h1>
          <p className="text-gray-600 mb-6">{getErrorMessage()}</p>
          <Link
            to="/"
            className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg shadow-md hover:shadow-lg hover:scale-105 transition-all duration-300"
          >
            <ArrowLeft className="w-5 h-5" />
            Quay về trang chủ
          </Link>
        </div>
      </div>
    </div>
  );
}
