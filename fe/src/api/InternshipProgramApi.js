import AxiosClient from "./AxiosClient";

const InternshipProgramApi = {
  getAll: () => {
    return AxiosClient.get("/internship-programs", { withAuth: true });
  },

  getInternshipProgram: (params) => {
    return AxiosClient.get("/internship-programs/get", {
      params,
      withAuth: true,
    });
  },

  create: (data) => {
    return AxiosClient.post("/internship-programs", data, { withAuth: true });
  },

  update: (data) => {
    return AxiosClient.put(`/internship-programs/${data.id}`, data, {
      withAuth: true,
    });
  },

  cancel: (id) => {
    return AxiosClient.patch(`/internship-programs/cancel/${id}`, null, {
      withAuth: true,
    });
  },

  publish: (id) => {
    return AxiosClient.patch(`/internship-programs/publish/${id}`, null, {
      withAuth: true,
    });
  },

  complete: (id) => {
    return AxiosClient.patch(`/internship-programs/complete/${id}`, null, {
      withAuth: true,
    });
  },
};

export default InternshipProgramApi;
