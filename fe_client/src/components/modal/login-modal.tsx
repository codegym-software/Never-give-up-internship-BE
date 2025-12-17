import { useState } from "react";
import { X } from "lucide-react";
import { Input } from "../ui/input";
import { useAuth } from "../../context/AuthContext";
import { loginApi, forgetPasswordApi, loginWithGoogleApi, linkGoogleAccountApi } from "../../api/authApi";
import { GoogleLogin, CredentialResponse } from "@react-oauth/google";
import { jwtDecode } from "jwt-decode";
import { toast } from "sonner";


interface LoginModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSwitchToRegister: () => void;
}

export function LoginModal({ isOpen, onClose, onSwitchToRegister }: LoginModalProps) {
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    identifier: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // State for Forget Password
  const [showForgetPassword, setShowForgetPassword] = useState(false);
  const [forgetPasswordData, setForgetPasswordData] = useState({ email: "", password: "" });
  const [forgetPasswordError, setForgetPasswordError] = useState("");
  const [isSubmittingForgetPassword, setIsSubmittingForgetPassword] = useState(false);

  // State for Account Linking
  const [showLinkAccountView, setShowLinkAccountView] = useState(false);
  const [googleCredential, setGoogleCredential] = useState<CredentialResponse | null>(null);
  const [linkAccountPassword, setLinkAccountPassword] = useState("");


  if (!isOpen) return null;

  const resetViews = () => {
    setShowForgetPassword(false);
    setShowLinkAccountView(false);
    setError("");
    setForgetPasswordError("");
  };

  const handleClose = () => {
    resetViews();
    onClose();
  };

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    
    setIsSubmitting(true);
    try {
      if (formData.password.length < 8) {
      setError("Mật khẩu phải có ít nhất 8 ký tự!");
      return;
    }
      const response = await loginApi({
        username: formData.identifier,
        password: formData.password,
      });
      if (response.data && response.data.accessToken && response.data.refreshToken) {
        login(response.data.accessToken, response.data.refreshToken);
        handleClose();
      }
    } catch (err) {
      setError("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.");
      console.error(err);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleGoogleLoginSuccess = async (credentialResponse: CredentialResponse) => {
    setError("");
    setIsSubmitting(true);
    try {
      if (credentialResponse.credential) {
        const response = await loginWithGoogleApi(credentialResponse.credential);
        if (response.data && response.data.accessToken && response.data.refreshToken) {
          login(response.data.accessToken, response.data.refreshToken);
          handleClose();
        }
      } else {
        throw new Error("No credential received from Google");
      }
    } catch (err: any) {
      if (err.response && err.response.status === 409) {
        // Account exists, needs linking
        setGoogleCredential(credentialResponse);
        setShowLinkAccountView(true);
        setError("Email này đã được đăng ký. Vui lòng nhập mật khẩu để liên kết.");
      } else {
        setError("Đăng nhập với Google thất bại.");
        console.error(err);
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleGoogleLoginError = () => {
    setError("Đăng nhập với Google thất bại.");
    console.error("Google Login Failed");
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
        const googleTokenPayload: { email: string } = jwtDecode(googleCredential.credential);
        const email = googleTokenPayload.email;

        // 2. Log in with email and password to verify ownership
        const loginResponse = await loginApi({ username: email, password: linkAccountPassword });

        // 3. If login is successful, set the new token and call link API
        if (loginResponse.data && loginResponse.data.accessToken && loginResponse.data.refreshToken) {
            login(loginResponse.data.accessToken, loginResponse.data.refreshToken); // Set auth header for the next request
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


  const handleForgetPasswordSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setForgetPasswordError("");
    if (!forgetPasswordData.email) {
      setForgetPasswordError("Vui lòng nhập email!");
      return;
    }
    if (forgetPasswordData.password.length < 8) {
      setForgetPasswordError("Mật khẩu mới phải có ít nhất 6 ký tự!");
      return;
    }
    setIsSubmittingForgetPassword(true);
    try {
      await forgetPasswordApi({
        email: forgetPasswordData.email,
        password: forgetPasswordData.password,
      });
      toast("Liên kết xác thực đã được gửi tới email của bạn!");
      setShowForgetPassword(false);
      setForgetPasswordData({ email: "", password: "" });
    } catch (err) {
      setForgetPasswordError("Gửi yêu cầu thất bại. Vui lòng kiểm tra email.");
      console.error(err);
    } finally {
      setIsSubmittingForgetPassword(false);
    }
  };

  const renderLoginView = () => (
    <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full mx-4 overflow-hidden">
      <div className="bg-gradient-to-r from-orange-500 to-orange-600 px-8 py-6 text-white">
        <div className="flex justify-between items-center">
          <div>
            <h2 className="text-2xl">Đăng nhập</h2>
            <p className="text-orange-100 text-sm mt-1">Chào mừng trở lại!</p>
          </div>
          <button title="Đóng cửa sổ đăng nhập" onClick={handleClose} className="w-8 h-8 flex items-center justify-center hover:bg-white/20 rounded-lg transition-colors">
            <X className="w-5 h-5" />
          </button>
        </div>
      </div>
      <form onSubmit={handleLoginSubmit} className="px-8 py-6 space-y-5">
        {error && <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-800 text-sm">{error}</div>}
        <div>
          <label className="block text-slate-700 mb-2">Email hoặc Tên đăng nhập <span className="text-orange-600">*</span></label>
          <Input name="identifier" type="text" required value={formData.identifier} onChange={(e) => setFormData({...formData, identifier: e.target.value})} placeholder="Nhập email hoặc tên đăng nhập" />
        </div>
        <div>
          <label className="block text-slate-700 mb-2">Mật khẩu <span className="text-orange-600">*</span></label>
          <Input name="password" type="password" required value={formData.password} onChange={(e) => setFormData({...formData, password: e.target.value})} placeholder="Nhập mật khẩu" />
        </div>
        <div className="text-right">
          <button type="button" onClick={() => { resetViews(); setShowForgetPassword(true); }} className="text-orange-600 hover:text-orange-700 text-sm">Quên mật khẩu?</button>
        </div>
        <button type="submit" disabled={isSubmitting} className="w-full px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg">
          {isSubmitting ? "Đang đăng nhập..." : "Đăng nhập"}
        </button>
        <div className="relative flex py-2 items-center justify-center">
          <div className="flex-grow border-t border-gray-300"></div><span className="flex-shrink mx-4 text-gray-400 text-sm">Hoặc</span><div className="flex-grow border-t border-gray-300"></div>
        </div>
        <div className="flex items-center justify-center">
          <GoogleLogin onSuccess={handleGoogleLoginSuccess} onError={handleGoogleLoginError} useOneTap shape="pill" width="300px" />
        </div>
        <p className="text-center text-slate-600 text-sm">
          Chưa có tài khoản? <button type="button" onClick={() => { resetViews(); onSwitchToRegister(); }} className="text-orange-600 hover:text-orange-700">Đăng ký ngay</button>
        </p>
      </form>
    </div>
  );

  const renderLinkAccountView = () => (
     <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full mx-4 overflow-hidden">
      <div className="bg-gradient-to-r from-blue-500 to-blue-600 px-8 py-6 text-white">
        <div className="flex justify-between items-center">
          <div>
            <h2 className="text-2xl">Liên kết tài khoản</h2>
            <p className="text-blue-100 text-sm mt-1">Xác nhận mật khẩu để liên kết với Google.</p>
          </div>
           <button title="Đóng cửa sổ đăng nhập"onClick={handleClose} className="w-8 h-8 flex items-center justify-center hover:bg-white/20 rounded-lg transition-colors">
            <X className="w-5 h-5" />
          </button>
        </div>
      </div>
      <form onSubmit={handleLinkAccountSubmit} className="px-8 py-6 space-y-5">
        {error && <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-800 text-sm">{error}</div>}
        <div>
          <label className="block text-slate-700 mb-2">Mật khẩu <span className="text-orange-600">*</span></label>
          <Input name="password" type="password" required value={linkAccountPassword} onChange={(e) => setLinkAccountPassword(e.target.value)} placeholder="Nhập mật khẩu của tài khoản đã có" />
        </div>
        <button type="submit" disabled={isSubmitting} className="w-full px-6 py-3 bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-lg">
          {isSubmitting ? "Đang liên kết..." : "Liên kết tài khoản"}
        </button>
         <p className="text-center text-slate-600 text-sm">
            <button type="button" onClick={() => { resetViews(); }} className="text-orange-600 hover:text-orange-700">Hủy bỏ</button>
        </p>
      </form>
    </div>
  );
  
  const renderForgetPasswordView = () => (
    // JSX for forget password, kept as is
    <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full mx-4 overflow-hidden">
        <div className="bg-gradient-to-r from-orange-500 to-orange-600 px-8 py-6 text-white">
            <div className="flex justify-between items-center">
                <div>
                    <h2 className="text-2xl">Quên mật khẩu</h2>
                    <p className="text-orange-100 text-sm mt-1">Nhập email và mật khẩu mới</p>
                </div>
                <button title="Đóng cửa sổ đăng nhập"onClick={() => { resetViews(); setShowForgetPassword(false); }} className="w-8 h-8 flex items-center justify-center hover:bg-white/20 rounded-lg transition-colors">
                    <X className="w-5 h-5" />
                </button>
            </div>
        </div>
        <form onSubmit={handleForgetPasswordSubmit} className="px-8 py-6 space-y-5">
            {forgetPasswordError && <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-800 text-sm">{forgetPasswordError}</div>}
            <div>
                <label className="block text-slate-700 mb-2">Email <span className="text-orange-600">*</span></label>
                <Input name="email" type="email" required value={forgetPasswordData.email} onChange={(e) => setForgetPasswordData({...forgetPasswordData, email: e.target.value})} placeholder="Nhập email của bạn" />
            </div>
            <div>
                <label className="block text-slate-700 mb-2">Mật khẩu mới <span className="text-orange-600">*</span></label>
                <Input name="password" type="password" required value={forgetPasswordData.password} onChange={(e) => setForgetPasswordData({...forgetPasswordData, password: e.target.value})} placeholder="Nhập mật khẩu mới" />
            </div>
            <button type="submit" disabled={isSubmittingForgetPassword} className="w-full px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg">
                {isSubmittingForgetPassword ? "Đang gửi..." : "Gửi yêu cầu"}
            </button>
            <p className="text-center text-slate-600 text-sm">
                Quay lại <button type="button" onClick={() => { resetViews(); setShowForgetPassword(false); }} className="text-orange-600 hover:text-orange-700">Đăng nhập</button>
            </p>
        </form>
    </div>
  );


  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/60 backdrop-blur-sm" onClick={handleClose} />
      
      {showLinkAccountView ? renderLinkAccountView() :
       showForgetPassword ? renderForgetPasswordView() :
       renderLoginView()
      }
    </div>
  );
}
