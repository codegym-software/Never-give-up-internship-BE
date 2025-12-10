🏰 Tổng quan hệ thống khi triển khai các dịch vụ trên AWS

Khi hệ thống được triển khai lên AWS, chúng ta sử dụng nhiều dịch vụ kết hợp với nhau để vận hành toàn bộ backend. Dưới đây là vai trò của từng thành phần:

🔹 EC2

EC2 là máy chủ chạy trên cloud, tương tự một server vật lý nhưng do AWS quản lý.
EC2 được dùng để:

Chạy backend (Spring Boot / NodeJS / bất kỳ service nào)

Tạo SSH Tunnel để truy cập RDS một cách an toàn

Chạy Docker / Docker Compose (nếu dùng container)

🔹 RDS

RDS là dịch vụ quản lý database của AWS.
Hệ thống sử dụng RDS MySQL và kết nối thông qua EC2 → RDS (vì RDS không mở public access).

🔹 Sơ đồ hoạt động tổng quan
Developer Laptop
        │
        │  (SSH Tunnel)
        ▼
      EC2 Server
        │
        │ (Private Subnet)
        ▼
        RDS MySQL


→ Laptop không truy cập trực tiếp vào RDS, mà đi xuyên qua EC2 để đảm bảo bảo mật.

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
    ubuntu@3.106.250.157


Lưu ý:

3.106.250.157 là Public IP của EC2 → có thể thay đổi khi restart.

Giữ nguyên cửa sổ SSH này, không được tắt.

🖥️ 3. Truy cập Database MySQL từ máy local

Mở một terminal khác và chạy:

mysql -h 127.0.0.1 -P 3307 -u admin -p


Nhập password của RDS khi được yêu cầu.
Nếu kết nối thành công → bạn đã truy cập RDS qua SSH Tunnel.

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