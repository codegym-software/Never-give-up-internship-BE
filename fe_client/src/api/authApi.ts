import axiosClient from './axiosClient';

const API_URL = '/api/v1/auth';

/**
 * Gửi yêu cầu đăng ký đến backend.
 * @param userData - Dữ liệu người dùng gồm { fullName, username, email, password }
 */
export const registerApi = (userData: any) => {
  return axiosClient.post(`${API_URL}/register`, userData);
};

/**
 * Gửi yêu cầu đăng nhập đến backend.
 * @param credentials - Thông tin đăng nhập gồm { username, password }
 */
export const loginApi = (credentials: any) => {
  // Backend của bạn đang dùng 'identifier' cho cả username và email
  const loginData = {
    identifier: credentials.username,
    password: credentials.password,
  };
  return axiosClient.post(`${API_URL}/login`, loginData);
};

/**
 * Gửi yêu cầu quên mật khẩu đến backend.
 * @param data - Dữ liệu gồm { email, password }
 */
export const forgetPasswordApi = (data: { email: string; password: string }) => {
  return axiosClient.post(`${API_URL}/forgetPassword`, data);
};

/**
 * Gửi ID Token của Google đến backend để xác thực.
 * @param idToken - ID Token nhận được từ Google.
 */
export const loginWithGoogleApi = (idToken: string) => {
  return axiosClient.post(`${API_URL}/google-login`, { idToken });
};

/**
 * Gửi ID Token của Google đến backend để liên kết với tài khoản hiện tại.
 * @param idToken - ID Token nhận được từ Google.
 */
export const linkGoogleAccountApi = (idToken: string) => {
  return axiosClient.post(`${API_URL}/link-google`, { idToken });
};

/**
 * Gửi yêu cầu đổi mật khẩu cho người dùng đã đăng nhập.
 * @param data - Dữ liệu gồm { oldPassword, newPassword }
 */
export const changePasswordApi = (data: any) => {
  return axiosClient.put(`${API_URL}/changePassword`, data);
};
