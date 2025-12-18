🏰 Tổng quan hệ thống khi triển khai các dịch vụ trên AWS

Khi hệ thống được triển khai lên AWS, chúng ta sử dụng nhiều dịch vụ để vận hành backend. Dưới đây là các thành phần chính và vai trò của chúng:

🔹 EC2 — Máy chủ ứng dụng

EC2 là máy chủ chạy trên cloud (giống một VPS).
EC2 được dùng để:

Chạy backend (Spring Boot / NodeJS / Docker / Nginx…)

Làm trung gian để tạo SSH Tunnel đến RDS

Lưu file log, file cấu hình, chạy script CICD, v.v.

🔹 Cách truy cập vào máy chủ EC2

Để truy cập EC2, bạn cần:

File key .pem (ví dụ: internship-sysney.pem)

Đúng địa chỉ Public IP của EC2

Truy cập bằng SSH:

ssh -i "internship-sysney.pem" ubuntu@3.6.27.169


Lưu ý quan trọng:

IP của EC2 có thể thay đổi nếu server bị restart (không có Elastic IP).

Username mặc định cho Ubuntu EC2: ubuntu

Lệnh SSH phải chạy ngay tại thư mục chứa file .pem

🔹 RDS — Database MySQL của AWS

RDS chứa database chính của hệ thống.
RDS không mở public access → chỉ EC2 mới có quyền truy cập trực tiếp.

Laptop → EC2 → RDS (qua private network)

🔹 Sơ đồ hoạt động tổng quan
Developer Laptop
        │
        │  SSH / SSH Tunnel
        ▼
      EC2 Server
        │
        │  (Private Connection)
        ▼
        RDS MySQL

Hướng dẫn truy cập Database RDS (AWS)

RDS là nơi lưu trữ database chính của hệ thống.
Hệ thống sử dụng port 3307 cho MySQL và truy cập thông qua SSH Tunnel từ EC2.

🔧 1. Kiểm tra & giải phóng port 3307 trên máy local

Kiểm tra chương trình nào đang chiếm cổng:

sudo lsof -i :3307


Nếu MySQL local đang chạy, tắt nó tạm thời:

sudo systemctl stop mysql


hoặc:

sudo systemctl stop mariadb

🚀 2. Tạo SSH Tunnel đến RDS thông qua EC2

Yêu cầu: có file key .pem để SSH vào EC2.

Chạy lệnh:

ssh -i "internship-sysney.pem" \
    -L 3307:internshipv3.chm8gaams2xg.ap-southeast-2.rds.amazonaws.com:3307 \
    ubuntu@3.6.27.169


Lưu ý:

3.106.250.157 là Public IP của EC2 → có thể thay đổi sau mỗi lần restart.

Cửa sổ SSH này phải giữ mở, vì đóng SSH → SSH tunnel mất.

🖥️ 3. Truy cập Database MySQL từ máy local

Mở một terminal khác và chạy:

mysql -h 127.0.0.1 -P 3307 -u admin -p


Nhập password của RDS.
Kết nối thành công → bạn đã truy cập RDS qua SSH Tunnel.

🧩 Sơ đồ kết nối (dễ hiểu)
Your Laptop (127.0.0.1:3307)
           │
           ▼
      SSH Tunnel
           │
           ▼
       EC2 Server
           │
           ▼
       AWS RDS (MySQL)

🎉 DONE — Bạn đã kết nối thành công đến RDS qua SSH Tunnel!