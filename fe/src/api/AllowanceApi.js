import AxiosClient from "./AxiosClient";

const AllowanceApi = {
 
  getAllowances : (filters) => {
    const url = "allowances";
    return AxiosClient.get(url, { params: filters, withAuth: true });
  },

  getMyHistory: (params) => {
    const url = "allowances/my-history";
    return AxiosClient.get(url, { params: params, withAuth: true });
  },

};


export default AllowanceApi;
