import { toast } from "react-toastify";
import ReportApi from "~/api/ReportApi";

export const getAttendanceSummary = async ({
  teamId,
  internshipProgramId,
  page,
}) => {
  const tId = teamId === 0 ? null : teamId;
  const iId = internshipProgramId === 0 ? null : internshipProgramId;
  try {
    const res = await ReportApi.getAttendanceSummary({
      teamId: tId,
      internshipProgramId: iId,
      page,
    });
    return res;
  } catch (err) {
    if (!err.response) {
      toast.error("Không thể kết nối đến server");
    }
  }
};

export const getInternAttendanceDetail = async (internId) => {
  try {
    const res = await ReportApi.getInternAttendanceDetail(internId);
    return res;
  } catch (err) {
    if (!err.response) {
      toast.error("Không thể kết nối đến server");
    }
  }
};

export const getFinalReport = async ({
  universityId,
  internshipProgramId,
  page,
}) => {
  const uId = universityId === 0 ? null : universityId;
  const iId = internshipProgramId === 0 ? null : internshipProgramId;
  try {
    const res = await ReportApi.getFinalReport({
      universityId: uId,
      internshipProgramId: iId,
      page,
    });
    return res;
  } catch (err) {
    if (!err.response) {
      toast.error("Không thể kết nối đến server");
    }
  }
};

export const chart = async ({ universityId, internshipProgramId }) => {
  const uId = universityId === 0 ? null : universityId;
  const iId = internshipProgramId === 0 ? null : internshipProgramId;
  try {
    const res = await ReportApi.chart({
      universityId: uId,
      internshipProgramId: iId,
    });
    return res;
  } catch (err) {
    if (!err.response) {
      toast.error("Không thể kết nối đến server");
    }
  }
};
