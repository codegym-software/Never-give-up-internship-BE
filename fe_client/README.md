# Trang chủ bán hàng (Community)

This is a code bundle for Trang chủ bán hàng (Community). The original project is available at https://www.figma.com/design/xspAMEpY7fVZ97f6nLzRQT/Trang-ch%E1%BB%A7-b%C3%A1n-h%C3%A0ng--Community-.

---

## Hướng dẫn Cài đặt và Chạy dự án

Làm theo các bước dưới đây để cài đặt và chạy dự án trên máy của bạn.

### 1. Cài đặt Dependencies

Mở terminal ở thư mục gốc của dự án và chạy lệnh sau để cài đặt tất cả các thư viện cần thiết:

```bash
npm install
```

### 2. Cấu hình Biến môi trường

Dự án cần một file biến môi trường để biết được địa chỉ của API backend.

a. Tạo một file mới có tên là `.env` ở thư mục gốc của dự án.

b. Mở file `.env` và thêm vào nội dung sau:

```
VITE_API_URL=http://localhost:8080
```

**Lưu ý:** Thay đổi `http://localhost:8080` thành địa chỉ API backend của bạn nếu nó khác.

### 3. Chạy Development Server

Sau khi cài đặt xong, chạy lệnh sau để khởi động server phát triển:

```bash
npm run dev
```

Ứng dụng sẽ khởi chạy và có thể truy cập tại địa chỉ `http://localhost:5173` (hoặc một cổng khác nếu cổng 5173 đã bị chiếm).

---

## Các câu lệnh có sẵn

- `npm run dev`: Chạy ứng dụng ở chế độ phát triển.
- `npm run build`: Build ứng dụng cho môi trường production. Các file tĩnh sẽ được tạo ra trong thư mục `build/`.# Never-give-up-internship-FE-Client-
