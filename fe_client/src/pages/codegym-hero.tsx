"use client";

import { ArrowRight, Code, Users, Award } from "lucide-react";
import { ImageWithFallback } from "../components/figma/ImageWithFallback";

interface CodegymHeroProps {
  onRegisterClick: () => void;
}

const accessToken = localStorage.getItem("accessToken")

export function CodegymHero({ onRegisterClick }: CodegymHeroProps) {
  return (
    <section
      id="home"
      className="relative min-h-screen flex items-center overflow-hidden bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 pt-20 scroll-mt-20"
    >
      {/* Background Image with Overlay */}
      <div className="absolute inset-0">
        <ImageWithFallback
          src="https://images.unsplash.com/photo-1635775017492-1eb935a082a4?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9ncmFtbWluZyUyMGNvZGluZyUyMGNvbXB1dGVyfGVufDF8fHx8MTc1OTQxNTA1M3ww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral"
          alt="Programming background"
          className="w-full h-full object-cover opacity-20"
        />
        <div className="absolute inset-0 bg-gradient-to-r from-slate-900/95 via-slate-900/80 to-slate-900/95" />
      </div>

      {/* Decorative Elements */}
      <div className="absolute inset-0 overflow-hidden">
        <div className="absolute top-20 left-10 w-72 h-72 bg-orange-500/10 rounded-full blur-3xl" />
        <div className="absolute bottom-20 right-10 w-96 h-96 bg-blue-500/10 rounded-full blur-3xl" />
      </div>

      <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20 w-full">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          {/* Left Content */}
          <div className="space-y-8">
            {/* Badge */}
            <div className="inline-flex items-center gap-2 px-4 py-2 bg-orange-500/10 border border-orange-500/20 rounded-full">
              <Award className="w-4 h-4 text-orange-400" />
              <span className="text-orange-400 text-sm">
                Chương trình đào tạo chuyên nghiệp
              </span>
            </div>

            {/* Main Heading */}
            <div className="space-y-4">
              <h1 className="text-5xl md:text-6xl lg:text-7xl text-white leading-tight">
                TRỞ THÀNH
                <br />
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-orange-600">
                  LẬP TRÌNH VIÊN
                </span>
                <br />
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-orange-600">
                  FULL-STACK
                </span>
              </h1>
              <p className="text-xl text-slate-300 max-w-xl">Cùng CODEGYM</p>
            </div>

            {/* Description */}
            <p className="text-lg text-slate-400 max-w-xl leading-relaxed">
              Chương trình đào tạo thực chiến với dự án thực tế, giảng viên giàu
              kinh nghiệm và cam kết hỗ trợ việc làm sau khóa học.
            </p>

            {/* CTA Button */}
            <div>
              {!accessToken ? (
                // Nếu KHÔNG có accessToken -> hiện 2 nút
                <div className="flex flex-col sm:flex-row gap-4">
                  <button
                    onClick={onRegisterClick}
                    className="group px-8 py-4 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-2xl hover:shadow-orange-500/40 transition-all duration-300 flex items-center justify-center gap-2"
                  >
                    Đăng ký
                    <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
                  </button>
                  <button
                    onClick={onRegisterClick}
                    className="px-8 py-4 bg-white/10 backdrop-blur-sm text-white border border-white/20 rounded-lg hover:bg-white/20 transition-all duration-300"
                  >
                    Tìm hiểu thêm
                  </button>
                </div>
              ) : (
                // Nếu CÓ accessToken -> hiện nội dung khác
                <div>
                  {/* Ví dụ */}
                  <p className="text-white"></p>
                </div>
              )}
            </div>

            {/* Stats */}
            <div className="flex flex-wrap gap-8 pt-8 border-t border-slate-700">
              <div>
                <div className="text-3xl text-white mb-1">500+</div>
                <div className="text-sm text-slate-400">Học viên</div>
              </div>
              <div>
                <div className="text-3xl text-white mb-1">95%</div>
                <div className="text-sm text-slate-400">Tỷ lệ việc làm</div>
              </div>
              <div>
                <div className="text-3xl text-white mb-1">50+</div>
                <div className="text-sm text-slate-400">
                  Đối tác doanh nghiệp
                </div>
              </div>
            </div>
          </div>

          {/* Right Content - Feature Cards */}
          <div className="hidden lg:grid gap-4">
            <div className="bg-white/5 backdrop-blur-sm border border-white/10 rounded-2xl p-6 hover:bg-white/10 transition-all duration-300">
              <div className="w-12 h-12 bg-orange-500/20 rounded-xl flex items-center justify-center mb-4">
                <Code className="w-6 h-6 text-orange-400" />
              </div>
              <h3 className="text-xl text-white mb-2">Học thực chiến</h3>
              <p className="text-slate-400">
                80% thời gian thực hành với dự án thực tế từ doanh nghiệp
              </p>
            </div>

            <div className="bg-white/5 backdrop-blur-sm border border-white/10 rounded-2xl p-6 hover:bg-white/10 transition-all duration-300">
              <div className="w-12 h-12 bg-orange-500/20 rounded-xl flex items-center justify-center mb-4">
                <Users className="w-6 h-6 text-orange-400" />
              </div>
              <h3 className="text-xl text-white mb-2">Mentor 1-1</h3>
              <p className="text-slate-400">
                Được hỗ trợ trực tiếp từ giảng viên giàu kinh nghiệm
              </p>
            </div>

            <div className="bg-white/5 backdrop-blur-sm border border-white/10 rounded-2xl p-6 hover:bg-white/10 transition-all duration-300">
              <div className="w-12 h-12 bg-orange-500/20 rounded-xl flex items-center justify-center mb-4">
                <Award className="w-6 h-6 text-orange-400" />
              </div>
              <h3 className="text-xl text-white mb-2">Cam kết việc làm</h3>
              <p className="text-slate-400">
                Hỗ trợ tìm việc và giới thiệu đến các doanh nghiệp đối tác
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Scroll Indicator */}
      <div className="absolute bottom-8 left-1/2 -translate-x-1/2 animate-bounce">
        <div className="w-6 h-10 border-2 border-white/30 rounded-full flex items-start justify-center p-2">
          <div className="w-1 h-3 bg-white/50 rounded-full" />
        </div>
      </div>
    </section>
  );
}
