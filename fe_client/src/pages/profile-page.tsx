"use client";

import { useEffect, useState, useRef } from "react";
import { useForm, Controller } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Save, CheckCircle2, Camera } from "lucide-react";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import { useAuth } from "../context/AuthContext";
import { updateUserInfoApi } from "../api/userApi";
import { toast } from "sonner";

// Kiểu dữ liệu cho form
interface ProfileFormData {
  fullName: string;
  phone: string;
  address: string;
}

export function ProfilePage() {
  // Lấy user và hàm cập nhật từ context
  const { user, updateUserContext, logout } = useAuth();
  const navigate = useNavigate();
  
  const [showSuccess, setShowSuccess] = useState(false);

  // State và Ref cho việc upload avatar
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [avatarPreview, setAvatarPreview] = useState<string | null>(null);
  const [selectedAvatarFile, setSelectedAvatarFile] = useState<File | null>(null);

  const {
    control,
    handleSubmit,
    reset,
    formState: { isSubmitting, errors },
  } = useForm<ProfileFormData>({
    defaultValues: {
      fullName: "",
      phone: "",
      address: "",
    },
  });

  // Effect 1: Cập nhật form khi user từ context thay đổi
  useEffect(() => {
    if (user) {
      reset({
        fullName: user.fullName || "",
        phone: user.phone || "",
        address: user.address || "",
      });
    } else {
        // Nếu không có user (ví dụ: token hết hạn), điều hướng về trang chủ
        navigate('/');
    }
  }, [user, reset, navigate]);
  
  // Effect 2: Dọn dẹp URL xem trước để tránh rò rỉ bộ nhớ
  useEffect(() => {
    return () => {
      if (avatarPreview) {
        URL.revokeObjectURL(avatarPreview);
      }
    };
  }, [avatarPreview]);


  // Hàm xử lý khi submit form cập nhật
  const onSubmit = async (data: ProfileFormData) => {
    try {
      const formData = new FormData();
      
      formData.append('fullName', data.fullName);
      formData.append('phone', data.phone);
      formData.append('address', data.address);

      if (selectedAvatarFile) {
        formData.append('avatarFile', selectedAvatarFile);
      }

      const response = await updateUserInfoApi(formData);
      
      // Cập nhật user trong context toàn cục
      updateUserContext(response.data);
      
      // Reset trạng thái upload sau khi thành công
      setAvatarPreview(null);
      setSelectedAvatarFile(null);
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }

      setShowSuccess(true);
      setTimeout(() => setShowSuccess(false), 3000);

    } catch (error) {
      console.error("Failed to update user info:", error);
      toast.error("Cập nhật thất bại. Vui lòng thử lại.");
    }
  };

  const getInitials = (name: string) => {
    return name?.split(' ').map(word => word[0]).join('').toUpperCase().slice(0, 2) || "U";
  };

  const handleAvatarClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      setSelectedAvatarFile(file);
      if (avatarPreview) {
        URL.revokeObjectURL(avatarPreview);
      }
      setAvatarPreview(URL.createObjectURL(file));
    }
  };

  // Nếu user chưa được tải từ context, hiển thị loading
  if (!user) {
    return <div className="min-h-screen bg-white pt-20 text-center">Đang tải thông tin...</div>;
  }

  return (
    <div className="min-h-screen bg-gray-50 pt-20">
       <input
        type="file"
        ref={fileInputRef}
        onChange={handleFileChange}
        className="hidden"
        accept="image/png, image/jpeg, image/gif"
      />
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-1">
        <Button variant="ghost" onClick={() => navigate('/')} className="flex items-center gap-2 text-slate-600 hover:text-orange-600 mb-8">
          <ArrowLeft className="w-5 h-5" />
          Quay về trang chủ
        </Button>

        <div className="max-w-3xl mx-auto">
          {showSuccess && (
            <div className="mb-8 p-4 bg-green-50 border border-green-200 rounded-lg flex items-center gap-3">
              <CheckCircle2 className="w-5 h-5 text-green-600" />
              <p className="text-green-800">Cập nhật thông tin thành công!</p>
            </div>
          )}

          <Card className="shadow-lg mb-6">
            <CardHeader className="text-center">
              <CardTitle className="text-3xl">Thông tin cá nhân</CardTitle>
              <CardDescription>Xem và cập nhật thông tin của bạn tại đây.</CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <div className="flex flex-col items-center space-y-4">
                  <div onClick={handleAvatarClick} className="relative cursor-pointer group">
                    <Avatar className="w-32 h-32 border-4 border-white shadow-md">
                      <AvatarImage src={avatarPreview || user.avatarUrl} alt={user.fullName} />
                      <AvatarFallback className="bg-gradient-to-br from-orange-500 to-orange-600 text-white text-4xl">
                        {getInitials(user.fullName)}
                      </AvatarFallback>
                    </Avatar>
                    <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-40 flex items-center justify-center rounded-full transition-all duration-300">
                      <Camera className="w-8 h-8 text-white opacity-0 group-hover:opacity-100" />
                    </div>
                  </div>
                </div>

                <div className="space-y-4 bg-slate-50 p-4 rounded-lg border">
                  <div>
                    <label className="text-sm font-medium text-gray-500">Email</label>
                    <p className="text-lg text-gray-800 font-mono">{user.email}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-gray-500">Tên đăng nhập</label>
                    <p className="text-lg text-gray-800 font-mono">{user.username}</p>
                  </div>
                </div>

                <hr />

                <div className="space-y-4">
                  <div>
                    <label htmlFor="fullName" className="block text-slate-700 mb-2 font-semibold">Họ và tên</label>
                    <Controller
                      name="fullName"
                      control={control}
                      rules={{ required: "Họ tên là bắt buộc" }}
                      render={({ field }) => <Input id="fullName" {...field} />}
                    />
                    {errors.fullName && <p className="text-red-500 text-sm mt-1">{errors.fullName.message}</p>}
                  </div>

                  <div>
                    <label htmlFor="phone" className="block text-slate-700 mb-2 font-semibold">Số điện thoại</label>
                    <Controller
                      name="phone"
                      control={control}
                      render={({ field }) => <Input id="phone" {...field} />}
                    />
                  </div>

                  <div>
                    <label htmlFor="address" className="block text-slate-700 mb-2 font-semibold">Địa chỉ</label>
                    <Controller
                      name="address"
                      control={control}
                      render={({ field }) => <Input id="address" {...field} />}
                    />
                  </div>
                </div>

                <Button type="submit" disabled={isSubmitting} className="w-full !mt-8 py-3 mb-4 border-2 border-black/30">
                  {isSubmitting ? (
                    <><div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin mr-2" /> Đang cập nhật...</>
                  ) : (
                     <div className="flex"><Save className="w-5 h-5 mr-2 mt-4" /> Cập nhật thông tin</div>
                  )}
                </Button>
              </form>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}