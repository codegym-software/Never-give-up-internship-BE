🏰 Tổng quan hệ thống khi triển khai trên AWS

Hệ thống backend được triển khai trên AWS với các thành phần chính sau:

🔹 EC2 — Application Server

EC2 là máy chủ cloud (tương tự VPS), được dùng để:

Chạy backend (Spring Boot / NodeJS / Docker / Nginx)

Làm trung gian tạo SSH Tunnel truy cập RDS

Lưu log, file cấu hình, script CI/CD

🔹 Truy cập EC2 Server
Yêu cầu

File key .pem (ví dụ: internship-sysney.pem)

Public IPv4 của EC2

Lệnh SSH
ssh -i "internship-sysney.pem" ubuntu@<EC2_PUBLIC_IP>

Lưu ý

Public IP có thể thay đổi khi EC2 stop/start

Username mặc định của Ubuntu EC2: ubuntu

Chạy lệnh tại thư mục chứa file .pem

🔹 RDS — MySQL Database

RDS chứa database chính của hệ thống

Không public access

Chỉ EC2 được phép truy cập qua private network

Sơ đồ kết nối
Developer Laptop
        │
        ▼
      EC2 Server
        │
        ▼
      RDS MySQL

🔹 Kết nối RDS qua SSH Tunnel
1️⃣ Giải phóng port 3307 trên máy local
sudo lsof -i :3307
sudo systemctl stop mysql
# hoặc
sudo systemctl stop mariadb

2️⃣ Tạo SSH Tunnel qua EC2
ssh -i "internship-sysney.pem" \
-L 3307:internshipv3.chm8gaams2xg.ap-southeast-2.rds.amazonaws.com:3307 \
ubuntu@<EC2_PUBLIC_IP>


⚠️ Không được tắt terminal này, nếu không tunnel sẽ mất.

3️⃣ Kết nối MySQL từ local
mysql -h 127.0.0.1 -P 3307 -u admin -p

🧩 Sơ đồ SSH Tunnel
Localhost (127.0.0.1:3307)
        │
        ▼
     SSH Tunnel
        │
        ▼
      EC2 Server
        │
        ▼
     AWS RDS

🔧 Hướng dẫn dành cho AWS Owner
Khi EC2 bị đổi Public IP (sau stop/start)
1️⃣ Cập nhật biến môi trường backend (trên EC2)
URL_FE=http://<EC2_PUBLIC_IP>:<PORT>
URL_BE=http://<EC2_PUBLIC_IP>:<PORT>


➡ Sau đó restart container backend:

docker compose down
docker compose up -d
2️⃣ Cập nhật biến môi trường frontend (GitHub Actions)

Vào repo → Settings → Secrets and variables

Key	Value
EC2_HOST	<EC2_PUBLIC_IP>
VITE_API_URL	http://<EC2_PUBLIC_IP>:<PORT>/api/v1
VITE_SOCKET_URL	http://<EC2_PUBLIC_IP>:<PORT>/ws
VITE_REFRESH_TOKEN_URL	http://<EC2_PUBLIC_IP>:<PORT>/api/v1/auth/refresh

➡ Sau đó build & push lại frontend image

3️⃣ (Khuyến nghị) Dùng Elastic IP

Tránh phải cập nhật IP mỗi lần EC2 restart

Phù hợp cho môi trường staging / production

Hoặc tạo một commit nhỏ để trigger GitHub Actions, pipeline CI/CD sẽ tự động build, test và deploy container mới lên EC2.