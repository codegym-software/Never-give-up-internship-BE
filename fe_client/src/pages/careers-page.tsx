"use client";

import { Code, Palette, Database, FileText, Upload } from "lucide-react";

const positions = [
  {
    id: "frontend",
    title: "Frontend Developer",
    icon: Code,
    description: "Xây dựng giao diện người dùng đẹp mắt và tương tác mượt mà với React, Vue.js hoặc Angular.",
    skills: ["HTML/CSS", "JavaScript", "React/Vue", "TypeScript", "Responsive Design"],
  },
  {
    id: "backend",
    title: "Backend Developer",
    icon: Database,
    description: "Phát triển API và xử lý logic phía server với Node.js, Java, Python hoặc .NET.",
    skills: ["Node.js/Java/Python", "Database", "REST API", "Microservices", "Cloud Services"],
  },
  {
    id: "uiux",
    title: "UI/UX Designer",
    icon: Palette,
    description: "Thiết kế trải nghiệm người dùng tuyệt vời và giao diện trực quan, thân thiện.",
    skills: ["Figma/Sketch", "User Research", "Wireframing", "Prototyping", "Design System"],
  },
  {
    id: "ba",
    title: "Business Analyst",
    icon: FileText,
    description: "Phân tích yêu cầu kinh doanh và chuyển đổi thành các giải pháp công nghệ hiệu quả.",
    skills: ["Requirements Analysis", "Documentation", "SQL", "Agile/Scrum", "Communication"],
  },
];

interface CareersPageProps {
  onApplicationFormClick: () => void;
}

export function CareersPage({ onApplicationFormClick }: CareersPageProps) {
  return (
    <section id="careers" className="scroll-mt-20">
      {/* Hero Section */}
      <div className="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h2 className="text-5xl md:text-6xl text-white mb-6">
              Cơ hội <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-orange-600">Nghề nghiệp</span>
            </h2>
            <p className="text-xl text-slate-300 max-w-3xl mx-auto">
              Tìm kiếm vị trí phù hợp với kỹ năng và đam mê của bạn
            </p>
          </div>
        </div>
      </div>

      {/* Positions */}
      <div className="py-20 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h3 className="text-4xl text-slate-900 mb-4">Vị trí tuyển dụng</h3>
            <p className="text-xl text-slate-600">Khám phá các cơ hội phát triển sự nghiệp tại CODEGYM</p>
          </div>

          <div className="grid md:grid-cols-2 gap-8 mb-16">
            {positions.map((position) => {
              const Icon = position.icon;
              return (
                <div
                  key={position.id}
                  className="bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-all duration-300 border-2 border-transparent hover:border-orange-500"
                >
                  <div className="flex items-start gap-4 mb-4">
                    <div className="w-14 h-14 bg-gradient-to-br from-orange-500 to-orange-600 rounded-xl flex items-center justify-center flex-shrink-0">
                      <Icon className="w-7 h-7 text-white" />
                    </div>
                    <div className="flex-1">
                      <h4 className="text-2xl text-slate-900 mb-2">{position.title}</h4>
                      <p className="text-slate-600">{position.description}</p>
                    </div>
                  </div>

                  <div className="mb-4">
                    <h5 className="text-slate-700 mb-2">Kỹ năng yêu cầu:</h5>
                    <div className="flex flex-wrap gap-2">
                      {position.skills.map((skill) => (
                        <span
                          key={skill}
                          className="px-3 py-1 bg-orange-50 text-orange-700 rounded-full text-sm"
                        >
                          {skill}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div className="py-20 bg-white">
        <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h3 className="text-4xl text-slate-900 mb-4">Sẵn sàng tham gia CODEGYM?</h3>
          <p className="text-xl text-slate-600 mb-8">
            Nộp hồ sơ ngay hôm nay để bắt đầu hành trình trở thành lập trình viên chuyên nghiệp
          </p>
          <button
            onClick={onApplicationFormClick}
            className="cursor-pointer inline-flex items-center gap-2 px-8 py-4 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-xl hover:shadow-orange-500/30 transition-all duration-300"
          >
            <Upload className="w-5 h-5" />
            Nộp hồ sơ ứng tuyển
          </button>
        </div>
      </div>
    </section>
  );
}
