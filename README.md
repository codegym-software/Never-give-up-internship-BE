
hướng dẫn truy cập database rds : đây là nơi lưu trữ database chính của hệ thống khi triển khai lên aws 


mặc đinh nếu cổng truy cập vào rds 3307 nếu máy local đang có xung đột chung cổng thì tạm thời dùng lệnh : 

Kiểm tra ai đang chiếm cổng 3306
sudo lsof -i :3306

Tắt MySQL local

Tuỳ distro:

sudo systemctl stop mysql

hoặc

sudo systemctl stop mariadb

b1: khởi chạy ec2 (yêu cầu : có file key để conect tới ec2), rồi mở cmd tại chính thư mục chứa file key rồi chạy lệnh sau :
  ssh -i "internship-sysney.pem" -L 3307:internshipv3.chm8gaams2xg.ap-southeast-2.rds.amazonaws.com:3307 ubuntu@3.106.250.157

  lưu ý : địa chỉ ip của ec2 (ubuntu@3.106.250.157) sẽ bị thay đổi nếu như ec2 bị reset lại , nên là hãy chắc chắn rằng địa chỉ ip là đúng 


  sau khi chạy lệnh trên thì để yên đó 

b2:  giao diện cli :    mở 1 cửa sổ cmd khác và chạy lệnh sau : 
mysql -h 127.0.0.1 -P 3307 -u admin -p

nếu yêu cầu pass thì nhập pass vào ==> bạn sẽ truy cập vào được database  . DONE 






