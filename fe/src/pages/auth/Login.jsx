// src/components/SignIn.jsx

import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { MdWbSunny, MdBrightnessMedium, MdNightsStay } from "react-icons/md"; // Import icon từ react-icons/md cho mặt trời, mặt trời chiều, mặt trăng
import { login, loginWithGoogle } from "../../services/AuthService";
import { GoogleLogin } from "@react-oauth/google";
import backgroundImage from '../../assets/background.jpg'; 
import "../../components/authLayout/Auth.css"

const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [greeting, setGreeting] = useState({ text: "Chào mừng trở lại", icon: null });
  const navigate = useNavigate();

  // Setup lời chào động dựa trên giờ hiện tại
  useEffect(() => {
    const currentHour = new Date().getHours();
    let greetingText = "Chào mừng trở lại hệ thống";
    let icon = null;

    if (currentHour >= 0 && currentHour < 12) {
      greetingText = "Buổi sáng tốt lành, chào mừng trở lại hệ thống";
      icon = <MdWbSunny style={{ color: '#ffd700', marginRight: '8px', fontSize: '20px' }} />;
    } else if (currentHour >= 12 && currentHour < 18) {
      greetingText = "Buổi chiều vui vẻ, chào mừng trở lại hệ thống";
      icon = <MdBrightnessMedium style={{ color: '#ffa500', marginRight: '8px', fontSize: '20px' }} />;
    } else {
      greetingText = "Buổi tối an lành, chào mừng trở lại với thống";
      icon = <MdNightsStay style={{ color: '#4b0082', marginRight: '8px', fontSize: '20px' }} />;
    }

    setGreeting({ text: greetingText, icon });
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    await login({ identifier, password, navigate });
  };

  const handleGoogleLogin = async (credentialResponse) => {
    await loginWithGoogle({
      idToken: credentialResponse.credential,
      navigate,
    });
  };

  return (
    <div className="auth-container">
      {/* Phần ảnh nền bên trái */}
      <div className="auth-background" style={{ backgroundImage: `url(${backgroundImage})` }}></div>
      
      {/* Phần form đăng nhập bên phải */}
      <div className="auth-form-wrapper">
        <div className="auth-form-container">
          <h2>ĐĂNG NHẬP</h2>
          <p className="subtitle sparkle-text" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            {greeting.icon}
            {greeting.text}
          </p>
          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <label htmlFor="email">Tài khoản</label>
              <input
                type="text"
                placeholder="Email hoặc tên đăng nhập"
                id="email"
                value={identifier}
                onChange={(e) => setIdentifier(e.target.value)}
              />
            </div>
            <div className="input-group">
              <label htmlFor="password">Mật khẩu</label>
              <div className="password-input">
                <input
                  type={showPassword ? "text" : "password"}
                  placeholder="Mật khẩu ít nhất 8 kí tự"
                  id="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <span
                  className="eye-icon"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? <FaEyeSlash /> : <FaEye />}
                </span>
              </div>
            </div>
            <div className="options-container">
              <Link to="/auth/forgot-password" className="auth-link">
                Quên mật khẩu?
              </Link>
            </div>
            <button type="submit" className="btn-primary">
              ĐĂNG NHẬP
            </button>
          </form>
          <div className="separator">HOẶC</div>
          <GoogleLogin
            onSuccess={handleGoogleLogin}
            onError={() => {
              console.log("Login Failed");
            }}
            useOneTap
          />
        </div>
      </div>
    </div>
  );
};

export default Login;