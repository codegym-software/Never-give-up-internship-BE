import axiosClient from "./axiosClient";

const API_URL = '/api/v1/majors';

export const getAllMajors = async () => {
  try {
    const response = await axiosClient.get(API_URL);
    return response.data || []; // Ensure an array is always returned
  } catch (error) {
    console.error("Error fetching majors:", error);
    throw error;
  }
};
