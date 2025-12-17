"use client";

import { Facebook, Twitter, Instagram, Linkedin, Youtube, Mail, Phone, MapPin } from "lucide-react";

export function CodegymFooter() {
  const currentYear = new Date().getFullYear();

  const scrollToSection = (sectionId: string) => {
    const element = document.getElementById(sectionId);
    if (element) {
      const offset = 80;
      const elementPosition = element.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.pageYOffset - offset;

      window.scrollTo({
        top: offsetPosition,
        behavior: "smooth",
      });
    }
  };

  return (
    <footer className="bg-slate-900 text-white">
      {/* Main Footer Content */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-12">
          {/* Company Info */}
          <div>
            <div className="flex items-center gap-3 mb-6">
              <div className="w-12 h-12 bg-gradient-to-br from-orange-500 to-orange-600 rounded-lg flex items-center justify-center">
                <span className="text-white text-lg">CG</span>
              </div>
              <div>
                <h3 className="text-xl text-white">CODEGYM</h3>
                <p className="text-xs text-slate-400">ACADEMY</p>
              </div>
            </div>
            <p className="text-slate-400 mb-6 leading-relaxed">
              Trung tâm đào tạo lập trình viên chuyên nghiệp hàng đầu Việt Nam với phương pháp học thực chiến.
            </p>
            <div className="flex gap-3">
              <a
                href="https://facebook.com"
                target="_blank"
                rel="noopener noreferrer"
                className="w-10 h-10 bg-slate-800 rounded-lg flex items-center justify-center hover:bg-orange-600 transition-colors duration-300"
              >
                <Facebook className="w-5 h-5" />
              </a>
              <a
                href="https://twitter.com"
                target="_blank"
                rel="noopener noreferrer"
                className="w-10 h-10 bg-slate-800 rounded-lg flex items-center justify-center hover:bg-orange-600 transition-colors duration-300"
              >
                <Twitter className="w-5 h-5" />
              </a>
              <a
                href="https://instagram.com"
                target="_blank"
                rel="noopener noreferrer"
                className="w-10 h-10 bg-slate-800 rounded-lg flex items-center justify-center hover:bg-orange-600 transition-colors duration-300"
              >
                <Instagram className="w-5 h-5" />
              </a>
              <a
                href="https://linkedin.com"
                target="_blank"
                rel="noopener noreferrer"
                className="w-10 h-10 bg-slate-800 rounded-lg flex items-center justify-center hover:bg-orange-600 transition-colors duration-300"
              >
                <Linkedin className="w-5 h-5" />
              </a>
              <a
                href="https://youtube.com"
                target="_blank"
                rel="noopener noreferrer"
                className="w-10 h-10 bg-slate-800 rounded-lg flex items-center justify-center hover:bg-orange-600 transition-colors duration-300"
              >
                <Youtube className="w-5 h-5" />
              </a>
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-lg text-white mb-6">Liên kết nhanh</h4>
            <ul className="space-y-3">
              <li>
                <button
                  onClick={() => scrollToSection("home")}
                  className="cursor-pointer text-slate-400 hover:text-orange-500 transition-colors"
                >
                  Trang chủ
                </button>
              </li>
              <li>
                <button
                  onClick={() => scrollToSection("about")}
                  className="cursor-pointer text-slate-400 hover:text-orange-500 transition-colors"
                >
                  Giới thiệu
                </button>
              </li>
              <li>
                <button
                  onClick={() => scrollToSection("careers")}
                  className="cursor-pointer text-slate-400 hover:text-orange-500 transition-colors"
                >
                  Cơ hội nghề nghiệp
                </button>
              </li>
              <li>
                <button
                  onClick={() => scrollToSection("status")}
                  className="cursor-pointer text-slate-400 hover:text-orange-500 transition-colors"
                >
                  Trạng thái CV
                </button>
              </li>
              <li>
                <button
                  onClick={() => scrollToSection("contact")}
                  className="cursor-pointer text-slate-400 hover:text-orange-500 transition-colors"
                >
                  Liên hệ
                </button>
              </li>
            </ul>
          </div>

          {/* Programs */}
          <div>
            <h4 className="text-lg text-white mb-6">Chương trình đào tạo</h4>
            <ul className="space-y-3">
              <li>
                <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors">
                  Full-Stack Web Development
                </a>
              </li>
              <li>
                <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors">
                  Mobile App Development
                </a>
              </li>
              <li>
                <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors">
                  Data Science & AI
                </a>
              </li>
              <li>
                <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors">
                  DevOps Engineer
                </a>
              </li>
              <li>
                <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors">
                  UI/UX Design
                </a>
              </li>
              <li>
                <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors">
                  Cloud Computing
                </a>
              </li>
            </ul>
          </div>

          {/* Contact Info */}
          <div>
            <h4 className="text-lg text-white mb-6">Liên hệ</h4>
            <ul className="space-y-4">
              <li className="flex items-start gap-3">
                <MapPin className="w-5 h-5 text-orange-500 flex-shrink-0 mt-1" />
                <span className="text-slate-400 text-sm leading-relaxed">
                  Nhà số 23, Lô TT-01, Khu đô thị MonCity<br />
                  P. Hàm Nghi, Hà Nội
                </span>
              </li>
              <li className="flex items-center gap-3">
                <Phone className="w-5 h-5 text-orange-500 flex-shrink-0" />
                <a href="tel:+84123456789" className="text-slate-400 hover:text-orange-500 transition-colors text-sm">
                  (+84) 123 456 789
                </a>
              </li>
              <li className="flex items-center gap-3">
                <Mail className="w-5 h-5 text-orange-500 flex-shrink-0" />
                <a href="mailto:info@codegym.vn" className="text-slate-400 hover:text-orange-500 transition-colors text-sm">
                  info@codegym.vn
                </a>
              </li>
            </ul>

            {/* Newsletter */}
            <div className="mt-6">
              <h5 className="text-slate-300 mb-3 text-sm">Đăng ký nhận tin</h5>
              <div className="flex gap-2">
                <input
                  type="email"
                  placeholder="Email của bạn"
                  className="flex-1 px-4 py-2 bg-slate-800 border border-slate-700 rounded-lg text-sm text-white placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-orange-500"
                />
                <button className="px-4 py-2 bg-gradient-to-r from-orange-500 to-orange-600 rounded-lg hover:shadow-lg hover:shadow-orange-500/30 transition-all duration-300">
                  <Mail className="w-5 h-5" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Bottom Bar */}
      <div className="border-t border-slate-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
          <div className="flex flex-col md:flex-row justify-between items-center gap-4">
            <p className="text-slate-400 text-sm text-center md:text-left">
              © {currentYear} CODEGYM ACADEMY. All rights reserved.
            </p>
            <div className="flex flex-wrap justify-center gap-6">
              <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors text-sm">
                Chính sách bảo mật
              </a>
              <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors text-sm">
                Điều khoản sử dụng
              </a>
              <a href="#" className="text-slate-400 hover:text-orange-500 transition-colors text-sm">
                Sitemap
              </a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}
