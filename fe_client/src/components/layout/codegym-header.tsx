"use client";

import { useState, useEffect, useRef } from "react";
import { Menu, X, User, FileText, LogOut, ChevronDown, KeyRound } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import ChangePasswordDialog from "../../pages/ChangePasswordDialog";
import { changePasswordApi } from "../../api/authApi";
import { Avatar, AvatarImage, AvatarFallback } from "../ui/avatar"; // Import Avatar components

interface CodegymHeaderProps {
  isLoggedIn: boolean;
  userName: string;
  avatarUrl?: string; // Add avatarUrl prop
  onLogout: () => void;
  onLoginClick: () => void;
  onRegisterClick: () => void;
  onApplicationFormClick: () => void;
  onStatusClick: () => void;
  onProfileClick: () => void;
  onContractClick: () => void;
  onChangePasswordClick: () => void;
}

export function CodegymHeader({
  isLoggedIn,
  userName,
  avatarUrl, // Destructure avatarUrl
  onLogout,
  onLoginClick,
  onRegisterClick,
  onApplicationFormClick,
  onStatusClick,
  onProfileClick,
  onContractClick,
  onChangePasswordClick
}: CodegymHeaderProps) {
  const navigate = useNavigate();
  const [isScrolled, setIsScrolled] = useState(false);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const [activeSection, setActiveSection] = useState("home");
  const [isAccountMenuOpen, setIsAccountMenuOpen] = useState(false);
  const accountMenuRef = useRef<HTMLDivElement>(null);

  const [isChangePasswordDialogOpen, setIsChangePasswordDialogOpen] = useState(false);
  const handleOpenChangePassword = () => setIsChangePasswordDialogOpen(true);
  const handleCloseChangePassword = () => setIsChangePasswordDialogOpen(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
      const sections = ["home", "about", "careers", "contact"];
      const scrollPosition = window.scrollY + 100;
      for (const section of sections) {
        const element = document.getElementById(section);
        if (element) {
          const { offsetTop, offsetHeight } = element;
          if (scrollPosition >= offsetTop && scrollPosition < offsetTop + offsetHeight) {
            setActiveSection(section);
            break;
          }
        }
      }
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (accountMenuRef.current && !accountMenuRef.current.contains(event.target as Node)) {
        setIsAccountMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const scrollToSection = (sectionId: string) => {
    const element = document.getElementById(sectionId);
    if (element) {
      const offset = 80;
      const elementPosition = element.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.pageYOffset - offset;
      window.scrollTo({ top: offsetPosition, behavior: "smooth" });
      setIsMobileMenuOpen(false);
    }
  };

  const handleNavigateHome = () => navigate("/");

  const getInitials = (name: string) => {
    return name?.split(' ').map(word => word[0]).join('').toUpperCase().slice(0, 2) || "U";
  };

  const handleChangePassword = async (oldPass: string, newPass: string) => {
    try {
      await changePasswordApi({ oldPassword: oldPass, newPassword: newPass });
      toast.success("Đổi mật khẩu thành công!");
      handleCloseChangePassword();
    } catch (error: any) {
      console.error("Failed to change password:", error);
      const errorMessage = error.response?.data?.message || "Đã có lỗi xảy ra. Vui lòng thử lại.";
      toast.error(errorMessage);
    }
  };

  return (
    <>
      <header
        className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 ${isScrolled
          ? "bg-white shadow-lg"
          : "bg-white/95 backdrop-blur-sm"
          }`}
      >
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-20">
            <button onClick={() => scrollToSection("home")} className="flex items-center">
              <div className="flex items-center gap-3 cursor-pointer">
                <div className="w-10 h-10 bg-gradient-to-br from-orange-500 to-orange-600 rounded-lg flex items-center justify-center">
                  <span className="text-white font-bold">CG</span>
                </div>
                <div onClick={handleNavigateHome}>
                  <h1 className="text-xl text-slate-900 tracking-tight">CODEGYM</h1>
                  <p className="text-xs text-slate-600">ACADEMY</p>
                </div>
              </div>
            </button>

            <nav className="hidden md:flex items-center gap-8">
              {/* Navigation Links */}
              <button onClick={() => scrollToSection("home")} className={`${activeSection === "home" ? "text-orange-600" : "text-slate-700"} hover:text-orange-600 transition-colors cursor-pointer`}>Trang chủ</button>
              <button onClick={() => scrollToSection("about")} className={`${activeSection === "about" ? "text-orange-600" : "text-slate-700"} hover:text-orange-600 transition-colors cursor-pointer`}>Giới thiệu</button>
              <button onClick={() => scrollToSection("careers")} className={`${activeSection === "careers" ? "text-orange-600" : "text-slate-700"} hover:text-orange-600 transition-colors cursor-pointer`}>Cơ hội nghề nghiệp</button>
              <button onClick={() => scrollToSection("contact")} className={`${activeSection === "contact" ? "text-orange-600" : "text-slate-700"} hover:text-orange-600 transition-colors cursor-pointer`}>Liên hệ</button>
              {isLoggedIn && <button onClick={onApplicationFormClick} className="text-slate-700 hover:text-orange-600 transition-colors">Hồ sơ</button>}

              {isLoggedIn ? (
                <div className="relative" ref={accountMenuRef}>
                  <button
                    onClick={() => setIsAccountMenuOpen(!isAccountMenuOpen)}
                    className="flex items-center gap-2 pl-2 pr-3 py-2 border-2 border-slate-200 text-slate-700 rounded-full hover:bg-slate-50 transition-colors"
                  >
                    <Avatar className="w-8 h-8">
                      <AvatarImage src={avatarUrl} />
                      <AvatarFallback className="bg-orange-500 text-white">
                        {getInitials(userName)}
                      </AvatarFallback>
                    </Avatar>
                    <span className="max-w-[120px] truncate font-medium">{userName}</span>
                    <ChevronDown className={`w-4 h-4 transition-transform ${isAccountMenuOpen ? 'rotate-180' : ''}`} />
                  </button>

                  {isAccountMenuOpen && (
                    <div className="absolute right-0 mt-2 w-56 bg-white rounded-lg shadow-xl border border-slate-200 py-2">
                      <button onClick={() => { setIsAccountMenuOpen(false); onProfileClick(); }} className="w-full px-4 py-2 text-left text-slate-700 hover:bg-slate-50 transition-colors flex items-center gap-3">
                        <User className="w-4 h-4" /> Quản lý thông tin cá nhân
                      </button>
                      <button onClick={() => { setIsAccountMenuOpen(false); onStatusClick(); }} className="w-full px-4 py-2 text-left text-slate-700 hover:bg-slate-50 transition-colors flex items-center gap-3">
                        <FileText className="w-4 h-4" /> Trạng thái đơn ứng tuyển
                      </button>
                      <div className="border-t border-slate-200 my-2" />
                      <button onClick={() => { setIsAccountMenuOpen(false); handleOpenChangePassword(); }} className="w-full px-4 py-2 text-left text-slate-700 hover:bg-slate-50 transition-colors flex items-center gap-3">
                        <KeyRound className="w-4 h-4" /> Đổi mật khẩu
                      </button>
                      <button onClick={() => { setIsAccountMenuOpen(false); onLogout(); }} className="w-full px-4 py-2 text-left text-red-600 hover:bg-red-50 transition-colors flex items-center gap-3">
                        <LogOut className="w-4 h-4" /> Đăng xuất
                      </button>
                    </div>
                  )}
                </div>
              ) : (
                <div className="flex items-center gap-3">
                  <button
                    onClick={onLoginClick}
                    className="cursor-pointer px-6 py-2.5 border-2 border-orange-500 text-orange-600 rounded-lg hover:bg-orange-50 transition-all duration-300"
                  >
                    Đăng nhập
                  </button>
                  <button
                    onClick={onRegisterClick}
                    className="cursor-pointer px-6 py-2.5 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-lg hover:shadow-orange-500/30 transition-all duration-300"
                  >
                    Đăng ký
                  </button>
                </div>
              )}
            </nav>
            <button
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              className="md:hidden p-2 text-slate-700 hover:text-orange-600"
            >
              {isMobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
            </button>
          </div>

          {/* Mobile Menu Button and Navigation */}
          {isMobileMenuOpen && (
            <div className="md:hidden py-4 border-t border-slate-200">
              <nav className="flex flex-col gap-4 cursor-pointer">
                <button
                  onClick={() => scrollToSection("home")}
                  className={`text-left ${activeSection === "home" ? "text-orange-600 cursor-pointer" : "text-slate-700"} hover:text-orange-600 transition-colors py-2 cursor-pointer`}
                >
                  Trang chủ
                </button>
                <button
                  onClick={() => scrollToSection("about")}
                  className={`text-left ${activeSection === "about" ? "text-orange-600 cursor-pointer" : "text-slate-700"} hover:text-orange-600 transition-colors py-2 cursor-pointer`}
                >
                  Giới thiệu
                </button>
                <button
                  onClick={() => scrollToSection("careers")}
                  className={`text-left ${activeSection === "careers" ? "text-orange-600 cursor-pointer" : "text-slate-700"} hover:text-orange-600 transition-colors py-2 cursor-pointer `}
                >
                  Cơ hội nghề nghiệp
                </button>
                <button
                  onClick={() => scrollToSection("contact")}
                  className={`text-left ${activeSection === "contact" ? "text-orange-600 cursor-pointer" : "text-slate-700"} hover:text-orange-600 transition-colors py-2 cursor-pointer`}
                >
                  Liên hệ
                </button>

                {isLoggedIn && (
                  <button
                    onClick={() => {
                      onApplicationFormClick();
                      setIsMobileMenuOpen(false);
                    }}
                    className="text-left text-slate-700 hover:text-orange-600 transition-colors py-2 cursor-pointer"
                  >
                    Hồ sơ
                  </button>
                )}

                {isLoggedIn ? (
                  <>
                    <div className="border-t border-slate-200 my-2" />
                    <div className="text-sm text-slate-600 py-2">{userName}</div>
                    <button
                      onClick={() => {
                        setIsMobileMenuOpen(false);
                        onProfileClick();
                      }}
                      className="text-left text-slate-700 hover:text-orange-600 transition-colors py-2 flex items-center gap-3 cursor-pointer"
                    >
                      <User className="w-4 h-4" />
                      Quản lý thông tin cá nhân
                    </button>
                    <button
                      onClick={() => {
                        onStatusClick();
                        setIsMobileMenuOpen(false);
                      }}
                      className="text-left text-slate-700 hover:text-orange-600 transition-colors py-2 flex items-center gap-3 cursor-pointer"
                    >
                      <FileText className="w-4 h-4" />
                      Trạng thái đơn ứng tuyển
                    </button>
                    <button
                      onClick={() => {
                        onChangePasswordClick();
                        setIsMobileMenuOpen(false);
                      }}
                      className="text-left text-slate-700 hover:text-slate-900 transition-colors py-2 flex items-center gap-3 cursor-pointer"
                    >
                      <KeyRound className="w-4 h-4" />
                      Đổi mật khẩu
                    </button>
                    <button
                      onClick={() => {
                        onLogout();
                        setIsMobileMenuOpen(false);
                      }}
                      className="text-left text-red-600 hover:text-red-700 transition-colors py-2 flex items-center gap-3 cursor-pointer"
                    >
                      <LogOut className="w-4 h-4" />
                      Đăng xuất
                    </button>
                  </>
                ) : (
                  <>
                    <button
                      onClick={() => {
                        onLoginClick();
                        setIsMobileMenuOpen(false);
                      }}
                      className="cursor-pointer px-6 py-2.5 border-2 border-orange-500 text-orange-600 rounded-lg hover:bg-orange-50 transition-all duration-300 text-center"
                    >
                      Đăng nhập
                    </button>
                    <button
                      onClick={() => {
                        onRegisterClick();
                        setIsMobileMenuOpen(false);
                      }}
                      className="cursor-pointer px-6 py-2.5 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-lg hover:shadow-orange-500/30 transition-all duration-300 text-center"
                    >
                      Đăng ký
                    </button>
                  </>
                )}
              </nav>
            </div>
          )}
        </div>
      </header>
      <ChangePasswordDialog
        isOpen={isChangePasswordDialogOpen}
        onClose={handleCloseChangePassword}
        onChangePassword={(oldPass, newPass, confirmPass) => {
          handleChangePassword(oldPass, newPass);
        }}
      />
    </>
  );
}
