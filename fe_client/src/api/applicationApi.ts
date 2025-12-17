import axiosClient from "./axiosClient";

const API_URL = "/api/v1/applications";

export const submitApplication = async (formData: FormData) => {
  const response = await axiosClient.post(API_URL, formData, {
    headers: {
      // Phải ghi đè header mặc định 'application/json' của axiosClient
      "Content-Type": "multipart/form-data",
    },
  });
  return response.data;
};

export const getMyApplication = async (token: string) => {
  const response = await axiosClient.get(`${API_URL}/me`);

  return response.data; // nếu chưa có server trả body rỗng -> data sẽ là null/undefined
};

export const withdrawApplication = (applicationId: string) => {
  return axiosClient.put(`${API_URL}/withdraw/${applicationId}`);
};

export const submitContract = (file: File) => {
  const formData = new FormData();
  formData.append("applicationContractFile", file);

  return axiosClient.put(`${API_URL}/submit-contract`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};
