![image](https://github.com/user-attachments/assets/9182c9bc-f3c2-41bd-bf24-3cb08cb9f762)
# MÔ HÌNH DỰ ÁN:
![image](https://github.com/user-attachments/assets/e90d97e9-d377-442a-80f8-4ee8e1a7f6b7)

# MỤC TIÊU:
Kiểm tra khả năng hoạt động và sự tích hợp của tính năng tạo người dùng mới, tạo đơn nhập kho trong hệ thống. Cụ thể, chúng ta sẽ xác minh rằng hệ thống có thể xử lý một cách chính xác các yêu cầu nhập thông tin của người dùng, đồng thời đảm bảo các tính năng như kiểm tra tính hợp lệ của dữ liệu, xử lý các tình huống lỗi, và thông báo kết quả cho người dùng khi thêm một người mới hay tạo một đơn nhập kho mới vào hệ thống. Tối ưu hóa được quá trình kiểm thử bằng cách thực thi các bộ testcase độc lập song song nhằm tiết kiệm thời gian mà không ảnh hưởng đến hiệu quả. Khi các test case hoàn tất, xuất báo cáo bằng công cụ Allure với giao thiện thân thiện người dùng, giúp chúng ta dễ dàng phân tích. Quy trình kiểm thử được tích hợp vào CI/CD với công cụ Jenkins và nền tảng GitHub. Khi developer cập nhật mã nguồn và push lên GitHub, Jenkins sẽ tự động kích hoạt quá trình kiểm thử. Báo cáo kết quả kiểm thử được tạo tự động và gửi đến nhóm phát triển để họ có thể nhanh chóng xác định và xử lý các lỗi nếu có.

# TESTCASES:
![image](https://github.com/user-attachments/assets/376e2ac1-3226-48dc-a767-82aeb628c3bc)
![image](https://github.com/user-attachments/assets/7d6368e8-0ac8-46e8-94ee-46bf59ac49df)
![image](https://github.com/user-attachments/assets/d183e70d-410a-474c-8eb6-eb77a01a1cce)
![image](https://github.com/user-attachments/assets/293e2ef9-76ab-4e99-9ff6-650b75f5f10e)

# KẾT QUẢ THỰC HIỆN:
## LẦN 1
### Kết quả tổng quan:
![image](https://github.com/user-attachments/assets/ffa31822-eef5-44fc-9d1e-bb7500a58683)
- Minh họa cho kết quả test case thành công:
![image](https://github.com/user-attachments/assets/44c587ca-b3be-4aca-80a6-162b930e2bfb)
- Minh họa cho test case thất bại:
![image](https://github.com/user-attachments/assets/fc6688de-61d5-4d7d-b0dc-87b2a1c7b1a0)
## LẦN 2
- Cập nhật tính năng verify email và thực hiện push code lên github:
![image](https://github.com/user-attachments/assets/fbc7de3d-88e1-4917-ba01-e076293a2a76)
- Lúc này Jenkins thông qua Webhook được cấu hình, nhận thấy sự thay đổi từ github sẽ tiến hành chạy kiểm thử:
![image](https://github.com/user-attachments/assets/8c438f4f-e34d-4ee4-ae9b-6498fea0be7e)
- Các bài kiểm thử khi được chạy xong sẽ gửi email thông báo cho người dùng:
![image](https://github.com/user-attachments/assets/eb93fe82-42f8-4e81-ba91-5eff44c7d4c0)
- Log trong quá trình thực thi được ghi lại:
  ![image](https://github.com/user-attachments/assets/bc0e6e73-bb64-4606-a22b-cd6dd9a1909e)
- Kết quả kiểm thử đã thay đổi so với lần kiểm thử đầu tiên:
![image](https://github.com/user-attachments/assets/59fff0a9-e534-4aff-a253-633d02caa2ce)

