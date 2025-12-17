"use client";

import { useState, useContext } from "react";
import { X } from "lucide-react";
import { Input } from "../ui/input";
import { registerApi, loginWithGoogleApi } from "../../api/authApi";
import { GoogleLogin, CredentialResponse } from "@react-oauth/google";
import { AuthContext } from "../../context/AuthContext";
import { toast } from "sonner";
import { jwtDecode } from "jwt-decode";
import { loginApi, linkGoogleAccountApi } from "../../api/authApi";
import { useAuth } from "../../context/AuthContext";

interface RegistrationModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSwitchToLogin: () => void;
}

export function RegistrationModal({
  isOpen,
  onClose,
  onSwitchToLogin,
}: RegistrationModalProps) {
  const [formData, setFormData] = useState({
    name: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const authContext = useContext(AuthContext);
  const { login } = useAuth();

  const [showLinkAccountView, setShowLinkAccountView] = useState(false);
  const [googleCredential, setGoogleCredential] =
    useState<CredentialResponse | null>(null);
  const [linkAccountPassword, setLinkAccountPassword] = useState("");
 const handleClose = () => {
    onClose();
  };

  const handleLinkAccountSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    if (!linkAccountPassword) {
      setError("Vui lòng nhập mật khẩu.");
      return;
    }
    if (!googleCredential || !googleCredential.credential) {
      setError("Lỗi không tìm thấy thông tin Google. Vui lòng thử lại.");
      return;
    }

    setIsSubmitting(true);
    try {
      // 1. Decode Google token to get email
      const googleTokenPayload: { email: string } = jwtDecode(
        googleCredential.credential
      );
      const email = googleTokenPayload.email;

      // 2. Log in with email and password to verify ownership
      const loginResponse = await loginApi({
        username: email,
        password: linkAccountPassword,
      });

      // 3. If login is successful, set the new token and call link API
      if (loginResponse.data && loginResponse.data.accessToken) {
        login(loginResponse.data.accessToken,loginResponse.data.refreshToken); // Set auth header for the next request
        await linkGoogleAccountApi(googleCredential.credential);
        toast.success("Liên kết tài khoản thành công!");
        handleClose();
      }
    } catch (err) {
      setError("Mật khẩu không chính xác. Không thể liên kết tài khoản.");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };
  const renderLinkAccountView = () => (
    <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full mx-4 overflow-hidden">
      <div className="bg-gradient-to-r from-blue-500 to-blue-600 px-8 py-6 text-white">
        <div className="flex justify-between items-center">
          <div>
            <h2 className="text-2xl">Liên kết tài khoản</h2>
            <p className="text-blue-100 text-sm mt-1">
              Xác nhận mật khẩu để liên kết với Google.
            </p>
          </div>
          <button
            onClick={handleClose}
            className="w-8 h-8 flex items-center justify-center hover:bg-white/20 rounded-lg transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
        </div>
      </div>
      <form onSubmit={handleLinkAccountSubmit} className="px-8 py-6 space-y-5">
        {error && (
          <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-800 text-sm">
            {error}
          </div>
        )}
        <div>
          <label className="block text-slate-700 mb-2">
            Mật khẩu <span className="text-orange-600">*</span>
          </label>
          <Input
            name="password"
            type="password"
            required
            value={linkAccountPassword}
            onChange={(e) => setLinkAccountPassword(e.target.value)}
            placeholder="Nhập mật khẩu của tài khoản đã có"
          />
        </div>
        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full px-6 py-3 bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-lg"
        >
          {isSubmitting ? "Đang liên kết..." : "Liên kết tài khoản"}
        </button>
        <p className="text-center text-slate-600 text-sm">
          <button
            type="button"
            onClick={() => {
              resetViews();
            }}
            className="text-orange-600 hover:text-orange-700"
          >
            Hủy bỏ
          </button>
        </p>
      </form>
    </div>
  );

  if (!isOpen) return null;
  if (showLinkAccountView) {
    return (
      <div className="fixed inset-0 z-50 flex items-center justify-center">
        {renderLinkAccountView()}
      </div>
    );
  }
 

  const resetViews = () => {
    setShowLinkAccountView(false);
    setError("");
  };

  const handleGoogleSuccess = async (
    credentialResponse: CredentialResponse
  ) => {
    if (credentialResponse.credential) {
      try {
        const response = await loginWithGoogleApi(
          credentialResponse.credential
        );
        authContext?.login(response.data.accessToken,response.data.refreshToken);
        onClose();
      } catch (err: any) {
        if (err.response && err.response.status === 409) {
          // Account exists, needs linking
          setGoogleCredential(credentialResponse);
          setShowLinkAccountView(true);
          setError(
            "Email này đã được đăng ký. Vui lòng nhập mật khẩu để liên kết."
          );
        } else {
          setError("Đăng nhập với Google thất bại.");
          console.error(err);
        }
      }
    } else {
      setError("Không nhận được thông tin từ Google.");
    }
  };

  const handleGoogleError = () => {
    setError("Đăng ký hoặc đăng nhập với Google thất bại.");
    console.error("Google Login Failed");
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (formData.password !== formData.confirmPassword) {
      setError("Mật khẩu xác nhận không khớp!");
      return;
    }

    if (formData.password.length < 6) {
      setError("Mật khẩu phải có ít nhất 8 ký tự!");
      return;
    }

    setIsSubmitting(true);

    try {
      await registerApi({
        fullName: formData.name,
        username: formData.username,
        email: formData.email,
        password: formData.password,
      });
      toast(
        "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản."
      );
      onSwitchToLogin(); // Tự động chuyển sang form đăng nhập
    } catch (err) {
      setError("Đăng ký thất bại. Tên đăng nhập hoặc email có thể đã tồn tại.");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError("");
  };


  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-black/60 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Modal */}
      <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full mx-4 overflow-hidden">
        {/* Header */}
        <div className="bg-gradient-to-r from-orange-500 to-orange-600 px-8 py-6 text-white">
          <div className="flex justify-between items-center">
            <div>
              <h2 className="text-2xl">Đăng ký tài khoản</h2>
              <p className="text-orange-100 text-sm mt-1">
                Tạo tài khoản để nộp CV ứng tuyển
              </p>
            </div>
            <button
              onClick={onClose}
              className="w-8 h-8 flex items-center justify-center hover:bg-white/20 rounded-lg transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Form */}
        <div className="px-8 py-6">
          {error && (
            <div className="p-3 mb-4 bg-red-50 border border-red-200 rounded-lg text-red-800 text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label htmlFor="reg-name" className="block text-slate-700 mb-2">
                Họ và tên <span className="text-orange-600">*</span>
              </label>
              <Input
                id="reg-name"
                name="name"
                type="text"
                required
                value={formData.name}
                onChange={handleChange}
                placeholder="Nhập họ và tên của bạn"
                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              />
            </div>

            <div>
              <label
                htmlFor="reg-username"
                className="block text-slate-700 mb-2"
              >
                Tên đăng nhập <span className="text-orange-600">*</span>
              </label>
              <Input
                id="reg-username"
                name="username"
                type="text"
                required
                value={formData.username}
                onChange={handleChange}
                placeholder="Tên đăng nhập duy nhất"
                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              />
            </div>

            <div>
              <label htmlFor="reg-email" className="block text-slate-700 mb-2">
                Email <span className="text-orange-600">*</span>
              </label>
              <Input
                id="reg-email"
                name="email"
                type="email"
                required
                value={formData.email}
                onChange={handleChange}
                placeholder="example@email.com"
                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              />
            </div>

            <div>
              <label
                htmlFor="reg-password"
                className="block text-slate-700 mb-2"
              >
                Mật khẩu <span className="text-orange-600">*</span>
              </label>
              <Input
                id="reg-password"
                name="password"
                type="password"
                required
                value={formData.password}
                onChange={handleChange}
                placeholder="Tối thiểu 8 ký tự"
                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              />
            </div>

            <div>
              <label
                htmlFor="reg-confirm-password"
                className="block text-slate-700 mb-2"
              >
                Xác nhận mật khẩu <span className="text-orange-600">*</span>
              </label>
              <Input
                id="reg-confirm-password"
                name="confirmPassword"
                type="password"
                required
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Nhập lại mật khẩu"
                className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
              />
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-lg hover:shadow-orange-500/30 transition-all duration-300 disabled:opacity-50"
            >
              {isSubmitting ? "Đang đăng ký..." : "Đăng ký"}
            </button>
            <div className="flex items-center my-5 justify-center">
              <div className="flex-grow border-t border-slate-300"></div>
              <span className="mx-4 text-slate-500">Hoặc</span>
              <div className="flex-grow border-t border-slate-300"></div>
            </div>
            <div className="flex items-center justify-center mb-4">
              <GoogleLogin
                onSuccess={handleGoogleSuccess}
                onError={handleGoogleError}
                shape="pill"
                width="300px"
              />
            </div>

            <p className="text-center text-slate-600 text-sm">
              Đã có tài khoản?{" "}
              <button
                type="button"
                onClick={onSwitchToLogin}
                className="text-orange-600 hover:text-orange-700"
              >
                Đăng nhập ngay
              </button>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}
