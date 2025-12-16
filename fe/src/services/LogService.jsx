import LogApi from "~/api/LogApi";
import { toast } from "react-toastify";
import { format } from "date-fns";

export const LogService = {
  getLogs: async (filters, page = 1, size = 10) => {
    try {
      const params = {
        page: page,
        size: size,
        searchName: filters.searchName || null, 
        affected: filters.affected || null,    
      };

      if (filters.fromDate) {
        params.fromDate = format(new Date(filters.fromDate), "dd-MM-yyyy");
      }
      if (filters.toDate) {
        params.toDate = format(new Date(filters.toDate), "dd-MM-yyyy");
      }

      const response = await LogApi.getActivityLogs(params);

      return response.data || response; 
    } catch (error) {
      console.error("Lỗi tải nhật ký:", error);
      toast.error("Không thể tải nhật ký hoạt động");
      return { content: [], totalPages: 0, totalElements: 0 };
    }
  },

  searchPerformers: async (keyword) => {
    try {
      const response = await LogApi.searchPerformers(keyword);
      return response.data || response || [];
    } catch {
      return [];
    }
  },
};