
import { CheckCircle2, ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";

export function VerificationSuccessPage() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 p-4">
      <div className="max-w-md w-full bg-white shadow-lg rounded-2xl p-8 border-t-4 border-green-500">
        <div className="text-center">
          <CheckCircle2 className="w-16 h-16 text-green-500 mx-auto mb-4" />
          <h1 className="text-3xl font-bold text-gray-800 mb-2">
            Xác thực thành công!
          </h1>
          <p className="text-gray-600 mb-6">
            Tài khoản của bạn đã được xác thực thành công.
          </p>
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
