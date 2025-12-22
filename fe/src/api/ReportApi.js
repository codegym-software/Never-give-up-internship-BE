// src/api/mockApi.js
import AxiosClient from "./AxiosClient";

const ReportApi = {
  sendReport: (sprintId, file) => {
    const formData = new FormData();
    formData.append("file", file);

    return AxiosClient.post(`${sprintId}/report`, formData, {
      withAuth: true,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  getMentorFeedback: (sprintId) => {
    return AxiosClient.get(`/sprints/evaluate/${sprintId}`, { withAuth: true });
  },

  getAttendanceSummary: (params) => {
    return AxiosClient.get(`reports/attendance-summary`, {
      params,
      withAuth: true,
    });
  },

  getInternAttendanceDetail: async (internId) => {
    const res = await AxiosClient.get(
      `/reports/interns/${internId}/attendance`,
      {
        withAuth: true,
      }
    );
    return res;
  },

  getFinalReport: (params) => {
    return AxiosClient.get("/reports/final-report", {
      params,
      withAuth: true,
    });
  },

  chart: (params) => {
    return AxiosClient.get("/reports/chart", {
      params,
      withAuth: true,
    });
  },
};

export default ReportApi;
