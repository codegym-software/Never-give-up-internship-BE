"use client";

import { useState } from "react";
import { Outlet, useNavigate, useLocation } from "react-router-dom";

import { Toaster, toast } from "sonner";
import { CodegymHeader } from "./components/layout/codegym-header";
import { CodegymFooter } from "./components/layout/codegym-footer";
import { RegistrationModal } from "./components/modal/registration-modal";
import { LoginModal } from "./components/modal/login-modal";
import { useAuth } from "./context/AuthContext";
import { submitApplication } from "./api/applicationApi";
import { ChatWidget } from "./components/chat/ChatWidget";
import webSocketService from "./api/webSocketService";

// These types can be moved to a dedicated types file later
interface UserData {
  name: string;
  username: string;
  email: string;
  password: string;
  phone: string;
  address: string;
  avatarUrl?: string;
}

interface Application {
  id: string;
  university: string;
  major: string;
  cvFileName: string;
  appliedDate: string;
  status: "submit" | "under_review" | "approved" | "confirmed" | "rejected" | "withdrawn";
  lastUpdate: string;
}

export type AppContextType = {
  isLoggedIn: boolean;
  userData: UserData | null;
  application: Application | null;
  onBack: () => void;
  onRegisterClick: () => void;
  onUpdate: (profileData: { name: string; phone: string; address: string; avatarUrl?: string }) => void;
  onSubmitApplication: (data: {
    universityId: string;
    majorId: string;
    internshipTermId: string;
    cvFile: File;
    internApplicationFile: File;
  }) => void;
  onApplicationFormClick: () => void;
  onRegisterClickForHero: () => void;
  onSwitchToContractPage: () => void;
};

export default function Root() {
  const navigate = useNavigate();
  const location = useLocation();
  // Get user and token from the upgraded AuthContext
  const { token, logout, user } = useAuth();

  const [isRegistrationModalOpen, setIsRegistrationModalOpen] = useState(false);
  const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);
  // userName state is no longer needed

  const handleLoginClick = () => setIsLoginModalOpen(true);
  const handleRegisterClick = () => setIsRegistrationModalOpen(true);
  const handleLoginClose = () => setIsLoginModalOpen(false);
  const handleRegistrationClose = () => setIsRegistrationModalOpen(false);
  const handleSwitchToLogin = () => {
    setIsRegistrationModalOpen(false);
    setIsLoginModalOpen(true);
  };
  const handleSwitchToRegister = () => {
    setIsLoginModalOpen(false);
    setIsRegistrationModalOpen(true);
  };
  const handleLogout = () => {
    webSocketService.disconnect();
    logout();
    navigate("/");
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleApplicationFormClick = () => {
    if (!token) {
      setIsLoginModalOpen(true);
    } else {
      navigate("/application-form");
      window.scrollTo({ top: 0, behavior: "smooth" });
    }
  };

  const handleChangePasswordClick = () => {
    // This might be handled by a dialog within the header or here
  };

  const handleStatusClick = () => navigate("/status");
  const handleProfileClick = () => navigate("/profile");
  const handleBackToHome = () => navigate("/");
  const handleSwitchToContractPage = () => navigate("/contract-page");

  const handleProfileUpdate = (profileData: any) => {
    console.log("Updating profile with:", profileData);
  };

  const handleSubmitApplication = async (data: {
    universityId: string;
    majorId: string;
    internshipTermId: string;
    cvFile: File;
    internApplicationFile: File;
  }) => {
    try {
      const formData = new FormData();
      formData.append("universityId", data.universityId);
      formData.append("majorId", data.majorId);
      formData.append("internshipTermId", data.internshipTermId);
      formData.append("cvFile", data.cvFile);
      formData.append("internApplicationFile", data.internApplicationFile);
      await submitApplication(formData);
      toast.success("Nộp hồ sơ thành công!");
      navigate("/status");
    } catch (error) {
      console.error("Failed to submit application:", error);
      toast.error("Nộp hồ sơ thất bại. Vui lòng thử lại.");
    }
  };

  const contextValue: AppContextType = {
    isLoggedIn: !!token,
    userData: null, // This context seems to be for a different purpose, leaving as is
    application: null,
    onBack: handleBackToHome,
    onRegisterClick: handleLoginClick,
    onUpdate: handleProfileUpdate,
    onSubmitApplication: handleSubmitApplication,
    onApplicationFormClick: handleApplicationFormClick,
    onRegisterClickForHero: handleRegisterClick,
    onSwitchToContractPage: handleSwitchToContractPage,
  };

  return (
    <div className="min-h-screen bg-white">
      <CodegymHeader
        isLoggedIn={!!token}
        // Pass user's full name or username to the header
        userName={user?.fullName || user?.username || ""}
        // Pass avatarUrl to the header
        avatarUrl={user?.avatarUrl}
        onLogout={handleLogout}
        onLoginClick={handleLoginClick}
        onRegisterClick={handleRegisterClick}
        onApplicationFormClick={handleApplicationFormClick}
        onStatusClick={handleStatusClick}
        onProfileClick={handleProfileClick}
        onChangePasswordClick={handleChangePasswordClick}
      />
      <main>
        <Outlet context={contextValue} />
      </main>

      {location.pathname === "/" && <CodegymFooter />}

      <RegistrationModal
        isOpen={isRegistrationModalOpen}
        onClose={handleRegistrationClose}
        onSwitchToLogin={handleSwitchToLogin}
      />

      <LoginModal
        isOpen={isLoginModalOpen}
        onClose={handleLoginClose}
        onSwitchToRegister={handleSwitchToRegister}
      />
      <ChatWidget />
      <Toaster richColors position="top-right" />
    </div>
  );
}