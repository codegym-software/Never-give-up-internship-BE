"use client";

import { Target, Users, Award, TrendingUp, Trophy } from "lucide-react";
import { ImageWithFallback } from "../components/figma/ImageWithFallback";
const teamMembers = [
  {
    name: "Dương Trọng Tấn",
    role: "Co-Founder",
    image: "/client/images/duongtrongtan.jpg",
  },
  {
    name: "Bùi Thị Phương Thảo",
    role: "Co-Founder",
    image: "/client/images/buithiphuongthao.jpg",
  },
  {
    name: "Nguyễn Khắc Nhật",
    role: "Co-Founder, Tổng giám đốc",
    image: "/client/images/nguyenkhacnhat.jpg",
  },
  {
    name: "Lê Thị Thanh Hằng",
    role: "Phó Tổng Giám đốc",
    image: "/client/images/lethithanhhang.jpg",
  },
  {
    name: "Đào Duy Thanh",
    role: "Phó Tổng Giám đốc",
    image: "/client/images/daoduythanh.jpg",
  },
  {
    name: "Trần Thị Tố Tâm",
    role: "Giám đốc CodeGym Đà Nẵng",
    image: "/client/images/tranthitolam.jpg",
  },
  {
    name: "Nguyễn Hữu Anh Khoa",
    role: "Giám đốc CodeGym Huế",
    image: "/client/images/nguyenhuuanhkhoa.jpg",
  },
  {
    name: "Nguyễn Thị Oanh",
    role: "Giám đốc CodeGym Online",
    image: "/client/images/nguyenthioanh.jpg",
  },
  {
    name: "Tạ Thu Hằng",
    role: "Điều phối Dự án CodeGym Thành Đô",
    image: "/client/images/tathuhang.jpg",
  },
  {
    name: "Trần Thị Vân",
    role: "Giám đốc Đào tạo CodeGym Thành Đô",
    image: "/client/images/tranthivan.jpg",
  },
  {
    name: "Lê Thị Châu",
    role: "Giám đốc Đào tạo CodeGym Việt Nam",
    image: "/client/images/lethichau.jpg",
  },
  {
    name: "Đoàn Phước Trung",
    role: "Giám đốc Đào tạo Đà Nẵng",
    image: "/client/images/doanphuoctrung.jpg",
  },
  {
    name: "Phạm Thuỳ Dương",
    role: "Giám đốc Đào tạo Sài Gòn",
    image: "/client/images/phamthuyduong.jpg",
  },
  {
    name: "Nguyễn Tài Tâm",
    role: "Giám đốc Đào tạo Quảng Trị",
    image: "/client/images/nguyentaitam.jpg",
  },
  {
    name: "Đặng Lệ Thuỷ",
    role: "Giám đốc Đào tạo Tester",
    image: "/client/images/danglethuy.jpg",
  },
  {
    name: "Đặng Huy Hòa",
    role: "Giám đốc Đào tạo CodeGym Online",
    image: "/client/images/danghuyhoa.jpg",
  },
  {
    name: "Nguyễn Khánh Tùng",
    role: "Trưởng phòng R&D",
    image: "/client/images/nguyenkhanhtung.jpg",
  },
    {
    name: "Lê Thị Diễm Hương",
    role: "Trưởng phòng Đảm bảo",
    image: "/client/images/lethidiemhuong.jpg",
  },
  {
    name: "Nguyễn Bình Sơn",
    role: "Trưởng phòng Công nghệ",
    image: "/client/images/nguyenbinhson.jpg",
  },
  {
    name: "Nguyễn Hồng Hạnh",
    role: "Trưởng phòng Marketing",
    image: "/client/images/nguyenhonghanh.jpg",
  },
  {
    name: "Trịnh Thị Trang",
    role: "Trưởng phòng Nhân sự",
    image: "/client/images/trinhthitrang.jpg",
  },
  {
    name: "Nguyễn Thị Thu Nga",
    role: "Trưởng nhóm TVTS Hà Nội",
    image: "/client/images/nguyenthithunga.jpg",
  },
  {
    name: "Hồ Diệu Hiền",
    role: "Trưởng nhóm TVTS Đà Nẵng",
    image: "/client/images/hodieuhien.jpg",
  },
  {
    name: "Phan Thị Thu Thảo",
    role: "Trưởng nhóm TVTS Quảng Trị",
    image: "/client/images/phanthithuthao.jpg",
  },
  {
    name: "Dương Thị Minh Châu",
    role: "Trưởng phòng TVTS Huế",
    image: "/client/images/duongthiminhchau.jpg",
  },
  {
    name: "Phạm Thị Kiều Trinh",
    role: "Trưởng nhóm sản phẩm ngắn hạn Hà Nội",
    image: "/client/images/phamthikieutrinh.jpg",
  },
  {
    name: "Lã Thị Cúc Thùy",
    role: "Trưởng phòng TVTS CodeGym Online",
    image: "/client/images/lathicucthuy.jpg",
  },
  {
    name: "Đỗ Thị Huyền Trang",
    role: "Trưởng nhóm Direct Sales",
    image: "/client/images/dothihuyentrang.jpg",
  },
  {
    name: "Nguyễn Song Gia Bảo",
    role: "Trưởng nhóm Marketing CodeGym Online",
    image: "/client/images/nguyensonggiabao.jpg",
  },
  {
    name: "Nguyễn Thị Hồng",
    role: "Trưởng nhóm Marketing Đà Nẵng",
    image: "/client/images/nguyenthihong.jpg",
  },
  {
    name: "Ngô Thị Hiền",
    role: "Trưởng nhóm Tài chính kế toán",
    image: "/client/images/ngothihien.jpg",
  },
  {
    name: "Phạm Hữu Hải",
    role: "Trưởng nhóm Marketing Sài Gòn",
    image: "/client/images/phamhuuhai.jpg",
  },
  {
    name: "Nguyễn Quỳnh Anh",
    role: "Thư ký TGĐ",
    image: "/client/images/nguyenquynhanh.jpg",
  },
  {
    name: "Phạm Thị Thúy",
    role: "Quản lý Đào tạo",
    image: "/client/images/phamthithuy.jpg",
  },
  {
    name: "Nguyễn Vũ Thành Tiến",
    role: "Trưởng nhóm Giảng viên",
    image: "/client/images/nguyenvuthanhtien.jpg",
  },
  {
    name: "Trương Tấn Hải",
    role: "Giảng viên",
    image: "/client/images/truongtanhai.jpg",
  },
  {
    name: "Trần Văn Chánh",
    role: "Giảng viên",
    image: "/client/images/tranvanchanh.jpg",
  },
  {
    name: "Đặng Chí Trung",
    role: "Giảng viên",
    image: "/client/images/dangchitrung.jpg",
  },
  {
    name: "Nguyễn Thanh Công",
    role: "Giảng viên",
    image: "/client/images/nguyenthanhcong.jpg",
  },
  {
    name: "Nguyễn Ngọc Quang",
    role: "Giảng viên",
    image: "/client/images/nguyenngocquang.jpg",
  },
  {
    name: "Trần Văn Hữu Trung",
    role: "Giảng viên",
    image: "/client/images/tranvanhuutrung.jpg",
  },
  {
    name: "Phạm Minh Sơn",
    role: "Giảng viên",
    image: "/client/images/phamminhson.jpg",
  },
  {
    name: "Vũ Thị Kiều Anh",
    role: "Giảng viên",
    image: "/client/images/vuthikieuanh.jpg",
  },
  {
    name: "Nguyễn Văn Toàn",
    role: "Giảng viên",
    image: "/client/images/nguyenvantoan.jpg",
  },
];
export function AboutSection() {
  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((word) => word[0])
      .join("")
      .toUpperCase()
      .slice(0, 2);
  };
  return (
    <section id="about" className="scroll-mt-20">
      {/* Hero Section */}
      <div className="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h2 className="text-5xl md:text-6xl text-white mb-6">
              Về{" "}
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-orange-600">
                CODEGYM ACADEMY
              </span>
            </h2>
            <p className="text-xl text-slate-300 max-w-3xl mx-auto">
              Chúng tôi cam kết đào tạo những lập trình viên xuất sắc, sẵn sàng
              cho thị trường công nghệ toàn cầu
            </p>
          </div>
        </div>
      </div>

      {/* Mission & Vision */}
      <div className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12">
            <div className="bg-gradient-to-br from-orange-50 to-white rounded-2xl p-8 border border-orange-100">
              <div className="w-16 h-16 bg-gradient-to-br from-orange-500 to-orange-600 rounded-xl flex items-center justify-center mb-6">
                <Target className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-3xl text-slate-900 mb-4">Sứ mệnh</h3>
              <p className="text-slate-600 leading-relaxed">
                Cung cấp chương trình đào tạo lập trình viên chất lượng cao, kết
                hợp lý thuyết và thực hành, giúp học viên tự tin bước vào môi
                trường làm việc chuyên nghiệp và phát triển sự nghiệp bền vững
                trong ngành công nghệ thông tin.
              </p>
            </div>

            <div className="bg-gradient-to-br from-blue-50 to-white rounded-2xl p-8 border border-blue-100">
              <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center mb-6">
                <TrendingUp className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-3xl text-slate-900 mb-4">Tầm nhìn</h3>
              <p className="text-slate-600 leading-relaxed">
                Trở thành trung tâm đào tạo lập trình viên hàng đầu tại Việt
                Nam, được công nhận bởi chất lượng đào tạo vượt trội, phương
                pháp giảng dạy sáng tạo và tỷ lệ học viên có việc làm cao nhất
                trong ngành.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Core Values */}
      <div className="py-20 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h3 className="text-4xl text-slate-900 mb-4">Giá trị cốt lõi</h3>
            <p className="text-xl text-slate-600">
              Những nguyên tắc định hướng mọi hoạt động của chúng tôi
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            <div className="bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-shadow">
              <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center mb-4">
                <Users className="w-6 h-6 text-orange-600" />
              </div>
              <h4 className="text-xl text-slate-900 mb-3">
                Học viên là trung tâm
              </h4>
              <p className="text-slate-600">
                Mọi quyết định và hoạt động đều hướng đến lợi ích và sự phát
                triển tốt nhất của học viên.
              </p>
            </div>

            <div className="bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-shadow">
              <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center mb-4">
                <Award className="w-6 h-6 text-orange-600" />
              </div>
              <h4 className="text-xl text-slate-900 mb-3">
                Chất lượng vượt trội
              </h4>
              <p className="text-slate-600">
                Cam kết duy trì tiêu chuẩn đào tạo cao nhất, không ngừng cải
                tiến chương trình học.
              </p>
            </div>

            <div className="bg-white rounded-xl p-8 shadow-lg hover:shadow-xl transition-shadow">
              <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center mb-4">
                <Target className="w-6 h-6 text-orange-600" />
              </div>
              <h4 className="text-xl text-slate-900 mb-3">Thực chiến</h4>
              <p className="text-slate-600">
                Học thông qua thực hành với dự án thực tế, chuẩn bị tốt nhất cho
                môi trường làm việc.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Stats */}
      <div className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
            <div className="text-center">
              <div className="text-5xl text-orange-600 mb-2">500+</div>
              <div className="text-slate-600">Học viên tốt nghiệp</div>
            </div>
            <div className="text-center">
              <div className="text-5xl text-orange-600 mb-2">95%</div>
              <div className="text-slate-600">Tỷ lệ việc làm</div>
            </div>
            <div className="text-center">
              <div className="text-5xl text-orange-600 mb-2">50+</div>
              <div className="text-slate-600">Đối tác doanh nghiệp</div>
            </div>
            <div className="text-center">
              <div className="text-5xl text-orange-600 mb-2">10+</div>
              <div className="text-slate-600">Năm kinh nghiệm</div>
            </div>
          </div>
        </div>
      </div>
      {/* Organizations & Awards */}
      <div className="py-20 bg-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h3 className="text-4xl text-slate-900 mb-4">
              Thành tích và Liên kết
            </h3>
            <p className="text-xl text-slate-600">
              Những cột mốc quan trọng và đối tác uy tín của CodeGym
            </p>
          </div>

          {/* Awards Grid */}
          <div className="grid md:grid-cols-3 gap-8">
            <div className="bg-white rounded-xl p-6 text-center shadow-lg hover:shadow-xl transition-all duration-300 hover:-translate-y-1">
              <div className="w-16 h-16 bg-gradient-to-br from-orange-400 to-orange-600 rounded-full flex items-center justify-center mx-auto mb-4">
                <Trophy className="w-8 h-8 text-white" />
              </div>
              <h4 className="text-xl text-slate-900 mb-2">Thành viên Vinasa</h4>
              <p className="text-slate-600 text-sm">
                Hiệp hội Phần mềm và Dịch vụ CNTT Việt Nam
              </p>
            </div>

            <div className="bg-white rounded-xl p-6 text-center shadow-lg hover:shadow-xl transition-all duration-300 hover:-translate-y-1">
              <div className="w-16 h-16 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center mx-auto mb-4">
                <Trophy className="w-8 h-8 text-white" />
              </div>
              <h4 className="text-xl text-slate-900 mb-2">VNIT Alliance</h4>
              <p className="text-slate-600 text-sm">
                Liên minh Công nghệ số Việt Nam
              </p>
            </div>

            <div className="bg-white rounded-xl p-6 text-center shadow-lg hover:shadow-xl transition-all duration-300 hover:-translate-y-1">
              <div className="w-16 h-16 bg-gradient-to-br from-purple-500 to-purple-600 rounded-full flex items-center justify-center mx-auto mb-4">
                <Trophy className="w-8 h-8 text-white" />
              </div>
              <h4 className="text-xl text-slate-900 mb-2">Agilead Global</h4>
              <p className="text-slate-600 text-sm">
                Tổ hợp Giáo dục Agilead Global
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Team Section */}
      <div className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h3 className="text-4xl text-slate-900 mb-4">
              Đội ngũ của CodeGym
            </h3>
            <p className="text-xl text-slate-600 max-w-3xl mx-auto">
              Những cán bộ tài năng luôn tận tụy đồng hành cùng giấc mơ trở
              thành lập trình viên chuyên nghiệp của các bạn học viên
            </p>
          </div>

          {/* Team Members Grid */}
          <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-12">
            {teamMembers.map((member, index) => (
              <div key={index} className="text-center group">
                <div className="mb-4 relative overflow-hidden rounded-full mx-auto w-30 h-30 md:w-40 md:h-40 bg-gradient-to-br from-orange-400 to-orange-600">
                  {member.image ? (
                    <>
                      <ImageWithFallback
                        src={member.image}
                        alt={member.name}
                        className="w-full h-full object-cover border-4 border-indigo-500"
                      />
                      <div className="absolute inset-0 bg-gradient-to-t from-slate-900/40 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
                    </>
                  ) : (
                    <>
                      <div className="w-full h-full flex items-center justify-center text-white text-3xl">
                        {getInitials(member.name)}
                      </div>
                      <div className="absolute inset-0 bg-gradient-to-t from-slate-900/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
                    </>
                  )}
                </div>
                <strong><h3 className="text-slate-900 mb-1">{member.name}</h3></strong>
                <p className="text-slate-600 text-lg">{member.role}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
