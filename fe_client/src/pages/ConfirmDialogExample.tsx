import { useState } from "react";
import {CheckCircle2 } from "lucide-react";
import { Button } from "../components/ui/button";

interface ConfirmDialogExampleProps {
  onConfirm: () => void;
  disabled?: boolean;
}

export default function ConfirmDialogExample({ onConfirm, disabled }: ConfirmDialogExampleProps) {
  const [showConfirm, setShowConfirm] = useState(false);

  const handleConfirm = () => {
    onConfirm();
    setShowConfirm(false);
  };

  return (
    <>
      {/* Nút mở dialog */}
      <button
        onClick={() => {
          if (disabled) return;
          setShowConfirm(true);
        }}
        disabled={disabled}
        className="w-full flex items-center justify-center gap-2 px-4 py-3 rounded-lg 
             bg-green-600 hover:bg-green-700 text-white font-semibold 
             transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
      >
        <CheckCircle2 className="w-5 h-5" />
        Xác nhận hợp đồng
      </button>
      {/* Overlay + hộp thoại */}
      {showConfirm && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm animate-fadeIn">
          <div className="bg-white rounded-2xl shadow-2xl w-[90%] max-w-md p-6 transform scale-100 animate-slideUp">
            {/* Header */}
            <div className="flex items-center gap-3 border-b pb-3 mb-4">
              </div>
              <div className="text-xl font-bold text-gray-800 text-center">
                Xác nhận hợp đồng
              </div>

            {/* Nội dung */}
            <div className="space-y-2 mb-6 text-gray-700">
              <p>Bạn có chắc chắn muốn xác nhận hợp đồng này không?</p>
              <p className="text-red-600 text-sm font-medium">
                ⚠️ Sau khi xác nhận, bạn sẽ không thể thay đổi.
              </p>
            </div>

            {/* Footer */}
            <div className="flex justify-between w-3/4 mx-auto mt-4">
              <button
                onClick={() => setShowConfirm(false)}
                className="px-4 py-2 rounded-lg bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium transition-colors"
              >
                ❌ Hủy bỏ
              </button>
              <button
                onClick={handleConfirm}
                className="px-4 py-2 rounded-lg bg-green-600 hover:bg-green-700 text-white font-medium shadow-sm transition-all"
              >
                ✅ Đồng ý
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
