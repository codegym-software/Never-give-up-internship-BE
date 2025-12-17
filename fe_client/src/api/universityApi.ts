import axiosClient from "./axiosClient";

const API_URL = '/universities';

export const getAllUniversities = async () => {
  try {
    const response = await axiosClient.get(API_URL);
    return response.data || []; // Ensure an array is always returned
  } catch (error) {
    console.error("Error fetching universities:", error);
    throw error;
  }
};
