import axiosClient from "./axiosClient";

const API_URL = '/api/v1/internship-programs';

export const getAllInternshipPrograms = async () => {
  try {
    const response = await axiosClient.get(API_URL);
    return response.data || [];
  } catch (error) {
    console.error("Error fetching internship programs:", error);
    throw error;
  }
};
