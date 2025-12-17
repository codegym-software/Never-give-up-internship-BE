import { Routes, Route } from "react-router-dom";
import Root from "./Root";
import { HomePage } from "./pages/HomePage";
import { ApplicationFormPage } from "./pages/application-form-page";
import { StatusPage } from "./pages/status-page";
import { ProfilePage } from "./pages/profile-page";
import { ContractPage } from "./pages/contract-page";
import { ProgramDetailPage } from "./pages/ProgramDetailPage";
import { VerificationSuccessPage } from "./pages/VerificationSuccessPage";
import { VerificationFailPage } from "./pages/VerificationFailPage";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Root />}>
        <Route index element={<HomePage />} />
        <Route path="application-form" element={<ApplicationFormPage />} />
        <Route path="status" element={<StatusPage />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="contract-page" element={<ContractPage />} />
        <Route path="/program/:programId" element={<ProgramDetailPage />} />
        <Route path="verification-success" element={<VerificationSuccessPage />} />
        <Route path="verification-fail" element={<VerificationFailPage />} />
      </Route>
    </Routes>
  );
}