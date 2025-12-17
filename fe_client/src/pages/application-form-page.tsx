import { useEffect, useState } from "react";
import { useOutletContext, useNavigate } from "react-router-dom";
import { AppContextType } from "../Root";
import { toast } from "sonner";
import { Upload, CheckCircle2, ArrowLeft } from "lucide-react";
import { getAllMajors } from "../api/majorApi";
import { getAllUniversities } from "../api/universityApi";
import { getAllInternshipPrograms } from "../api/internshipProgramApi";
import { submitApplication, getMyApplication } from "../api/applicationApi";
import Select from "react-select";

// --- Định nghĩa kiểu dữ liệu ---
interface Major {
  id: number;
  name: string;
}
interface University {
  id: number;
  name: string;
}
interface InternshipProgram {
  id: number;
  timeStart: string;
  timeEnd: string;
  name: string;
}

export function ApplicationFormPage() {
  const navigate = useNavigate();
  const {
    isLoggedIn,
    onBack,
    onRegisterClick,
  } = useOutletContext<AppContextType>();

  const [hasSubmitted, setHasSubmitted] = useState(false);
  const [isCheckingStatus, setIsCheckingStatus] = useState(true);

  // --- State ---
  const [cvFile, setCvFile] = useState<File | null>(null);
  const [applicationLetter, setApplicationLetter] = useState<File | null>(null);
  const [formData, setFormData] = useState({
    universityId: "",
    majorId: "",
    internshipTermId: "",
  });
  const [submitStatus, setSubmitStatus] = useState<
    "idle" | "submitting" | "success"
  >("idle");

  const [majors, setMajors] = useState<Major[]>([]);
  const [universities, setUniversities] = useState<University[]>([]);
  const [programs, setPrograms] = useState<InternshipProgram[]>([]);

  const [loadingMajors, setLoadingMajors] = useState(true);
  const [loadingUniversities, setLoadingUniversities] = useState(true);
  const [loadingPrograms, setLoadingPrograms] = useState(true);

  const [errorMajors, setErrorMajors] = useState<string | null>(null);
  const [errorUniversities, setErrorUniversities] = useState<string | null>(null);
  const [errorPrograms, setErrorPrograms] = useState<string | null>(null);

  // --- Kiểm tra đăng nhập ngay từ đầu ---
  useEffect(() => {
    if (!isLoggedIn) {
      toast.error("Vui lòng đăng nhập để nộp hồ sơ!");
      navigate("/");
      onRegisterClick();
    }
  }, [isLoggedIn, navigate, onRegisterClick]);

  // --- Fetch reference data ---
  useEffect(() => {
    const fetchMajors = async () => {
      try {
        const data = await getAllMajors();
        setMajors(data);
      } catch {
        setErrorMajors("Failed to load majors.");
      } finally {
        setLoadingMajors(false);
      }
    };
    const fetchUniversities = async () => {
      try {
        const data = await getAllUniversities();
        setUniversities(data);
      } catch {
        setErrorUniversities("Failed to load universities.");
      } finally {
        setLoadingUniversities(false);
      }
    };
    const fetchPrograms = async () => {
      try {
        const data = await getAllInternshipPrograms();
        setPrograms(data);
      } catch {
        setErrorPrograms("Failed to load internship programs.");
      } finally {
        setLoadingPrograms(false);
      }
    };

    fetchMajors();
    fetchUniversities();
    fetchPrograms();
  }, []);

  // --- Check server whether user already submitted ---
  useEffect(() => {
    const checkSubmitted = async () => {
      setIsCheckingStatus(true);
      const token = localStorage.getItem("accessToken");
      if (!token) {
        setIsCheckingStatus(false);
        setHasSubmitted(false);
        return;
      }

      try {
        const data = await getMyApplication(token);
        if (!Array.isArray(data) || data.length === 0) {
          setHasSubmitted(false);
        } else {
          const hasNonRejectedvsWITHDRAWN = data.some(
            (app) =>
              app.internshipApplicationStatus !== "REJECTED" &&
              app.internshipApplicationStatus !== "WITHDRAWN"
          );

          if (hasNonRejectedvsWITHDRAWN) {
            setHasSubmitted(true);
          } else {
            setHasSubmitted(false);
          }
        }
      } catch (err) {
        console.error("Failed to check application:", err);
        setHasSubmitted(false);
      } finally {
        setIsCheckingStatus(false);
      }
    };

    checkSubmitted();
  }, []);

  // --- Handlers ---
  const handleCvFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      // Kiểm tra kích thước file (5MB)
      if (file.size > 5 * 1024 * 1024) {
        toast.error("File CV không được vượt quá 5MB!");
        return;
      }
      setCvFile(file);
    }
  };

  const handleApplicationLetterChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      // Kiểm tra kích thước file (5MB)
      if (file.size > 5 * 1024 * 1024) {
        toast.error("File đơn xin thực tập không được vượt quá 5MB!");
        return;
      }
      setApplicationLetter(file);
    }
  };

  const handleSelectChange = (name: string, value: string) => {
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Kiểm tra đăng nhập
    if (!isLoggedIn) {
      toast.error("Vui lòng đăng nhập để nộp hồ sơ!");
      onRegisterClick();
      return;
    }

    // Kiểm tra files
    if (!cvFile) {
      toast.error("Vui lòng tải lên CV!");
      return;
    }
    if (!applicationLetter) {
      toast.error("Vui lòng tải lên Đơn xin thực tập!");
      return;
    }

    // Kiểm tra form data
    if (!formData.internshipTermId || !formData.universityId || !formData.majorId) {
      toast.error("Vui lòng chọn đầy đủ thông tin!");
      return;
    }

    setSubmitStatus("submitting");

    try {
      const payload = new FormData();
      payload.append("internshipTermId", formData.internshipTermId);
      payload.append("universityId", formData.universityId);
      payload.append("majorId", formData.majorId);
      payload.append("cvFile", cvFile);
      payload.append("internApplicationFile", applicationLetter);

      await submitApplication(payload);

      toast.success("Nộp hồ sơ thành công!");
      setSubmitStatus("success");
      setHasSubmitted(true);
      
      // Redirect sau 2 giây
      setTimeout(() => {
        navigate("/status");
      }, 2000);
    } catch (error: any) {
      console.error(error);
      const errorMessage = error?.response?.data?.message || "Gửi hồ sơ thất bại!";
      toast.error(errorMessage);
      setSubmitStatus("idle");
    }
  };

  // Loading state
  if (isCheckingStatus) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-white">
        <svg
          className="w-12 h-12 animate-spin text-orange-500"
          viewBox="0 0 50 50"
        >
          <circle
            cx="25"
            cy="25"
            r="20"
            stroke="currentColor"
            strokeWidth="5"
            fill="none"
            strokeDasharray="80"
            strokeDashoffset="60"
          ></circle>
        </svg>
        <p className="text-slate-600 mt-4">Đang kiểm tra trạng thái...</p>
      </div>
    );
  }

  // Already submitted
  if (hasSubmitted) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-white">
        <CheckCircle2 className="w-16 h-16 text-green-500 mb-4" />
        <h1 className="text-3xl font-semibold text-slate-800 mb-2">
          Bạn đã nộp hồ sơ rồi
        </h1>
        <p className="text-slate-600 mb-6">
          Mỗi người chỉ được nộp hồ sơ thực tập một lần.
        </p>
        <button
          onClick={onBack}
          className="px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg shadow-md hover:shadow-lg hover:scale-105 transition-all duration-300"
        >
          Quay lại trang chính
        </button>
      </div>
    );
  }

  // Form validation helper
  const isFormValid = cvFile && applicationLetter && 
    formData.internshipTermId && formData.universityId && formData.majorId;

  return (
    <div className="min-h-screen bg-white pt-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <button
          onClick={onBack}
          className="flex items-center gap-2 text-slate-600 hover:text-orange-600 transition-colors mb-8"
        >
          <ArrowLeft className="w-5 h-5" />
          Quay lại
        </button>

        <div className="max-w-2xl mx-auto">
          <div className="bg-white rounded-2xl shadow-xl p-8 border border-slate-200">
            {submitStatus === "success" ? (
              <div className="text-center py-12">
                <CheckCircle2 className="w-16 h-16 text-green-500 mx-auto mb-6" />
                <h2 className="text-2xl font-semibold text-slate-900 mb-4">
                  Nộp hồ sơ thành công!
                </h2>
                <p className="text-slate-600 mb-6">
                  Cảm ơn bạn đã nộp hồ sơ. Chúng tôi sẽ xem xét và phản hồi sớm nhất.
                </p>
                <button
                  onClick={() => navigate("/status")}
                  className="px-6 py-3 bg-orange-500 text-white rounded-lg hover:bg-orange-600"
                >
                  Xem trạng thái hồ sơ
                </button>
              </div>
            ) : (
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="text-center mb-12">
                  <h1 className="text-4xl text-slate-900 mb-4">
                    Nộp hồ sơ ứng tuyển
                  </h1>
                  <p className="text-xl text-slate-600">
                    Điền thông tin của bạn
                  </p>
                </div>

                {/* Internship Term */}
                <div>
                  <label className="block text-slate-700 mb-2">
                    Kỳ thực tập <span className="text-orange-600">*</span>
                  </label>
                  <Select
                    isSearchable
                    isDisabled={loadingPrograms || !!errorPrograms}
                    placeholder={
                      loadingPrograms
                        ? "Đang tải các kỳ thực tập..."
                        : errorPrograms
                        ? `Lỗi: ${errorPrograms}`
                        : "Chọn kỳ thực tập"
                    }
                    value={
                      programs
                        .map((p) => ({
                          value: String(p.id),
                          label: p.name,
                        }))
                        .find((opt) => opt.value === String(formData.internshipTermId)) || null
                    }
                    onChange={(opt) =>
                      handleSelectChange("internshipTermId", opt?.value ?? "")
                    }
                    options={programs.map((p) => ({
                      value: String(p.id),
                      label: p.name,
                    }))}
                    className="react-select-container"
                    classNamePrefix="react-select"
                  />
                </div>

                {/* University Select */}
                <div>
                  <label className="block text-slate-700 mb-2">
                    Tên trường <span className="text-orange-600">*</span>
                  </label>
                  <Select
                    isSearchable
                    isDisabled={loadingUniversities || !!errorUniversities}
                    placeholder={
                      loadingUniversities
                        ? "Đang tải các trường..."
                        : errorUniversities
                        ? `Lỗi: ${errorUniversities}`
                        : "Chọn trường đại học"
                    }
                    value={
                      universities
                        .map((u) => ({
                          value: String(u.id),
                          label: u.name,
                        }))
                        .find((opt) => opt.value === String(formData.universityId)) || null
                    }
                    onChange={(opt) =>
                      handleSelectChange("universityId", opt?.value ?? "")
                    }
                    options={universities.map((u) => ({
                      value: String(u.id),
                      label: u.name,
                    }))}
                    className="react-select-container"
                    classNamePrefix="react-select"
                  />
                </div>

                {/* Major Select */}
                <div>
                  <label className="block text-slate-700 mb-2">
                    Tên chuyên ngành <span className="text-orange-600">*</span>
                  </label>
                  <Select
                    isSearchable
                    isDisabled={loadingMajors || !!errorMajors}
                    placeholder={
                      loadingMajors
                        ? "Đang tải chuyên ngành..."
                        : errorMajors
                        ? `Lỗi: ${errorMajors}`
                        : "Chọn chuyên ngành"
                    }
                    value={
                      majors
                        .map((m) => ({
                          value: String(m.id),
                          label: m.name,
                        }))
                        .find((opt) => opt.value === String(formData.majorId)) || null
                    }
                    onChange={(opt) =>
                      handleSelectChange("majorId", opt?.value ?? "")
                    }
                    options={majors.map((m) => ({
                      value: String(m.id),
                      label: m.name,
                    }))}
                    className="react-select-container"
                    classNamePrefix="react-select"
                  />
                </div>

                <div className="bg-white rounded-xl shadow-lg border border-slate-200 p-8 mb-6">
                  <div className="grid md:grid-cols-2 gap-6">
                    {/* CV Upload */}
                    <div className="border-2 border-dashed border-slate-300 rounded-lg p-8 text-center hover:border-orange-500 transition-colors">
                      <h3 className="text-lg text-slate-900 mb-3">📄 Upload CV</h3>
                      <input
                        id="cv"
                        type="file"
                        accept=".pdf,.doc,.docx"
                        onChange={handleCvFileChange}
                        className="hidden"
                      />
                      <label htmlFor="cv" className="cursor-pointer flex flex-col items-center">
                        <Upload className="w-12 h-12 text-slate-400 mb-4" />
                        {cvFile ? (
                          <div className="text-slate-900 break-all">{cvFile.name}</div>
                        ) : (
                          <>
                            <div className="text-slate-700 mb-2">
                              Click để chọn file hoặc kéo thả file vào đây
                            </div>
                            <div className="text-sm text-slate-500">
                              PDF, DOC, DOCX (Tối đa 5MB)
                            </div>
                          </>
                        )}
                      </label>
                    </div>

                    {/* Application Letter Upload */}
                    <div className="border-2 border-dashed border-slate-300 rounded-lg p-8 text-center hover:border-orange-500 transition-colors">
                      <h3 className="text-lg text-slate-900 mb-3">
                        📤 Upload đơn xin thực tập
                      </h3>
                      <input
                        id="application"
                        type="file"
                        accept=".pdf,.doc,.docx"
                        onChange={handleApplicationLetterChange}
                        className="hidden"
                      />
                      <label htmlFor="application" className="cursor-pointer flex flex-col items-center">
                        <Upload className="w-12 h-12 text-slate-400 mb-4" />
                        {applicationLetter ? (
                          <div className="text-slate-900 break-all">
                            {applicationLetter.name}
                          </div>
                        ) : (
                          <>
                            <div className="text-slate-700 mb-2">
                              Click để chọn file hoặc kéo thả file vào đây
                            </div>
                            <div className="text-sm text-slate-500">
                              PDF, DOC, DOCX (Tối đa 5MB)
                            </div>
                          </>
                        )}
                      </label>
                    </div>
                  </div>
                </div>

                {/* Submit button */}
                <button
                  type="submit"
                  disabled={submitStatus === "submitting" || !isFormValid}
                  className="w-full px-8 py-4 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-xl transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-3"
                >
                  {submitStatus === "submitting" ? (
                    <>
                      <svg className="w-5 h-5 animate-spin" viewBox="0 0 50 50">
                        <circle
                          cx="25"
                          cy="25"
                          r="20"
                          stroke="currentColor"
                          strokeWidth="5"
                          fill="none"
                          strokeDasharray="80"
                          strokeDashoffset="60"
                        ></circle>
                      </svg>
                      <span>Đang gửi...</span>
                    </>
                  ) : (
                    <>
                      <CheckCircle2 className="w-5 h-5" />
                      <span>Nộp hồ sơ</span>
                    </>
                  )}
                </button>

                {/* Validation hint */}
                {!isFormValid && (
                  <p className="text-center text-sm text-slate-500">
                    Vui lòng điền đầy đủ thông tin và tải lên cả 2 file
                  </p>
                )}
              </form>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}