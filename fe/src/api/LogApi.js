import AxiosClient from "./AxiosClient";

const LogApi = {
  getActivityLogs: (params) => {
    return AxiosClient.get("/logs", {
      withAuth: true,
      params: params, //fromDate, toDate, affected, searchName, page, size
    });
  },

  searchPerformers: (keyword) => {
    return AxiosClient.get("/logs/performers", {
      withAuth: true,
      params: { keyword },
    });
  },
};

export default LogApi;