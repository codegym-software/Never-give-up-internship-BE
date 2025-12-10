# internship_demo_cicd_v2


hướng dẫn truy cập database rds : đây là nơi lưu trữ database chính của hệ thống khi triển khai lên aws 


b1: khởi chạy ec2 (yêu cầu : có file key để conect tới ec2), rồi mở cmd tại chính thư mục chứa file key rồi chạy lệnh sau :
  ssh -i "internship-sysney.pem" -L 3306:internshipv3.chm8gaams2xg.ap-southeast-2.rds.amazonaws.com:3306 ubuntu@3.106.250.157

  lưu ý : địa chỉ ip của ec2 (ubuntu@3.106.250.157) sẽ bị thay đổi nếu như ec2 bị reset lại , nên là hãy chắc chắn rằng địa chỉ ip là đúng 

  sau khi chạy thành công thì lấy cái địa chỉ ip ở dòng này  :  IPv4 address for ens5: (gọi tắt là IP a )

  sau khi chạy lệnh trên thì để yên đó 
b2:  giao diện cli :    mở 1 cửa sổ cmd khác và chạy lệnh sau : 
mysql -h (IP a ) -P 3306 -u admin -p

nếu yêu cầu pass thì nhập pass vào ==> bạn sẽ truy cập vào được database  . DONE 






