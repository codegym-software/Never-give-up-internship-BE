import axiosClient from './axiosClient';

const USER_API_URL = '/users';

/**
 * Lấy thông tin chi tiết của người dùng đang đăng nhập.
 * Interceptor sẽ tự động đính kèm token.
 */
export const getUserInfoApi = () => {
  return axiosClient.get(`${USER_API_URL}/info`);
};

/**
 * Cập nhật thông tin của người dùng đang đăng nhập.
 * @param updateData - Dữ liệu FormData chứa thông tin và file ảnh.
 * Interceptor sẽ tự động đính kèm token.
 */
export const updateUserInfoApi = (updateData: FormData) => {
  return axiosClient.put(`${USER_API_URL}/info`, updateData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};
