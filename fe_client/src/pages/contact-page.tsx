"use client";

import { Mail, Phone, MapPin, Clock, Send } from "lucide-react";
import { useState } from "react";
import { Input } from "../components/ui/input";
import { Textarea } from "../components/ui/textarea";
import { ImageWithFallback } from "../components/figma/ImageWithFallback";

export function ContactPage() {
  const [formData, setFormData] = useState({
    name: "",
    phone: "",
    email: "",
    content: "",
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitMessage, setSubmitMessage] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSubmitMessage("");

    // Simulate form submission
    setTimeout(() => {
      setSubmitMessage("Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm nhất có thể.");
      setIsSubmitting(false);
      setFormData({ name: "", phone: "", email: "", content: "" });
    }, 1500);
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <section id="contact" className="scroll-mt-20">
      {/* Hero Section */}
      <div className="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h2 className="text-5xl md:text-6xl text-white mb-6">
              Liên hệ <span className="text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-orange-600">với chúng tôi</span>
            </h2>
            <p className="text-xl text-slate-300 max-w-3xl mx-auto">
              Chúng tôi luôn sẵn sàng hỗ trợ bạn
            </p>
          </div>
        </div>
      </div>

      {/* Contact Content */}
      <div className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-12">
            {/* Contact Form */}
            <div>
            <div className="bg-white rounded-2xl shadow-xl p-8 lg:p-10 border border-slate-200">
              <h3 className="text-2xl text-slate-900 mb-6">Gửi tin nhắn</h3>
              
              {submitMessage && (
                <div className="mb-6 p-4 bg-green-50 border border-green-200 rounded-lg">
                  <p className="text-green-800">{submitMessage}</p>
                </div>
              )}

              <form onSubmit={handleSubmit} className="space-y-6">
                <div>
                  <label htmlFor="contact-name" className="block text-slate-700 mb-2">
                    Họ và tên <span className="text-orange-600">*</span>
                  </label>
                  <Input
                    id="contact-name"
                    name="name"
                    type="text"
                    required
                    value={formData.name}
                    onChange={handleChange}
                    placeholder="Nhập họ và tên của bạn"
                    className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
                  />
                </div>

                <div>
                  <label htmlFor="contact-phone" className="block text-slate-700 mb-2">
                    Số điện thoại <span className="text-orange-600">*</span>
                  </label>
                  <Input
                    id="contact-phone"
                    name="phone"
                    type="tel"
                    required
                    value={formData.phone}
                    onChange={handleChange}
                    placeholder="Nhập số điện thoại của bạn"
                    className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
                  />
                </div>

                <div>
                  <label htmlFor="contact-email" className="block text-slate-700 mb-2">
                    Email <span className="text-orange-600">*</span>
                  </label>
                  <Input
                    id="contact-email"
                    name="email"
                    type="email"
                    required
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Nhập địa chỉ email của bạn"
                    className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent"
                  />
                </div>

                <div>
                  <label htmlFor="contact-content" className="block text-slate-700 mb-2">
                    Nội dung <span className="text-orange-600">*</span>
                  </label>
                  <Textarea
                    id="contact-content"
                    name="content"
                    rows={4}
                    required
                    value={formData.content}
                    onChange={handleChange}
                    placeholder="Để lại lời nhắn hoặc câu hỏi của bạn..."
                    className="w-full px-4 py-3 border border-slate-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent resize-none"
                  />
                </div>

                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="w-full px-8 py-4 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-xl hover:shadow-orange-500/30 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
                >
                  {isSubmitting ? (
                    <>
                      <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                      Đang gửi...
                    </>
                  ) : (
                    <>
                      <Send className="w-5 h-5" />
                      Gửi tin nhắn
                    </>
                  )}
                </button>
              </form>
            </div>
            {/* Team Image */}
              <div className="rounded-2xl overflow-hidden shadow-xl">
                <ImageWithFallback
                  src="https://images.unsplash.com/photo-1521737852567-6949f3f9f2b5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxzb2Z0d2FyZSUyMGRldmVsb3BtZW50JTIwdGVhbXxlbnwxfHx8fDE3NTkzODE4NjJ8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral"
                  alt="CODEGYM Team"
                  className="w-full h-64 object-cover"
                />
              </div>
            </div>

            {/* Company Info & Map */}
            <div className="space-y-8">
              {/* Company Info */}
              <div className="bg-slate-900 rounded-2xl p-8 lg:p-10 text-white">
                <h3 className="text-2xl mb-6">Thông tin liên hệ</h3>
                
                <div className="space-y-6">
                  <div className="flex items-start gap-4">
                    <div className="w-12 h-12 bg-orange-500/20 rounded-xl flex items-center justify-center flex-shrink-0">
                      <MapPin className="w-6 h-6 text-orange-400" />
                    </div>
                    <div>
                      <h4 className="text-slate-200 mb-1">Địa chỉ</h4>
                      <p className="text-slate-400">
                        Nhà số 23, Lô TT-01, Khu đô thị MonCity<br />
                        P. Hàm Nghi, Hà Nội
                      </p>
                    </div>
                  </div>

                  <div className="flex items-start gap-4">
                    <div className="w-12 h-12 bg-orange-500/20 rounded-xl flex items-center justify-center flex-shrink-0">
                      <Phone className="w-6 h-6 text-orange-400" />
                    </div>
                    <div>
                      <h4 className="text-slate-200 mb-1">Hotline</h4>
                      <p className="text-slate-400">
                        (+84) 123 456 789<br />
                        (+84) 987 654 321
                      </p>
                    </div>
                  </div>

                  <div className="flex items-start gap-4">
                    <div className="w-12 h-12 bg-orange-500/20 rounded-xl flex items-center justify-center flex-shrink-0">
                      <Mail className="w-6 h-6 text-orange-400" />
                    </div>
                    <div>
                      <h4 className="text-slate-200 mb-1">Email</h4>
                      <p className="text-slate-400">
                        info@codegym.vn<br />
                        support@codegym.vn
                      </p>
                    </div>
                  </div>

                  <div className="flex items-start gap-4">
                    <div className="w-12 h-12 bg-orange-500/20 rounded-xl flex items-center justify-center flex-shrink-0">
                      <Clock className="w-6 h-6 text-orange-400" />
                    </div>
                    <div>
                      <h4 className="text-slate-200 mb-1">Giờ làm việc</h4>
                      <p className="text-slate-400">
                        Thứ 2 - Thứ 6: 8:00 - 17:00<br />
                        Thứ 7 - Chủ nhật: Nghỉ
                      </p>
                    </div>
                  </div>
                </div>
              </div>

              {/* Map */}
              <div className="bg-white rounded-2xl overflow-hidden shadow-xl border border-slate-200">
                <div className="aspect-video relative">
                  <iframe
                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3723.944345092454!2d105.76183307508104!3d21.034912780615922!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x313454bee40409db%3A0xac6ab720d3555559!2zUC4gSMOgbSBOZ2hpLCBOYW0gVOG7qyBMacOqbSwgSMOgIE7hu5lpLCBWaWV0bmFt!5e0!3m2!1sen!2sus!4v1760502186086!5m2!1sen!2sus"
                    width="100%"
                    height="100%"
                    style={{ border: 0 }}
                    allowFullScreen
                    loading="lazy"
                    referrerPolicy="no-referrer-when-downgrade"
                    className="absolute inset-0"
                  />
                </div>
              </div>

              
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
