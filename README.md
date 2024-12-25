MÔ HÌNH DỰ ÁN:
![image](https://github.com/user-attachments/assets/e90d97e9-d377-442a-80f8-4ee8e1a7f6b7)

MỤC TIÊU: Kiểm tra khả năng hoạt động và sự tích hợp của tính năng tạo người dùng mới, tạo đơn nhập kho trong hệ thống. Cụ thể, chúng ta sẽ xác minh rằng hệ thống có thể xử lý một cách chính xác các yêu cầu nhập thông tin của người dùng, đồng thời đảm bảo các tính năng như kiểm tra tính hợp lệ của dữ liệu, xử lý các tình huống lỗi, và thông báo kết quả cho người dùng khi thêm một người mới hay tạo một đơn nhập kho mới vào hệ thống. Tối ưu hóa được quá trình kiểm thử bằng cách thực thi các bộ testcase độc lập song song nhằm tiết kiệm thời gian mà không ảnh hưởng đến hiệu quả. Khi các test case hoàn tất, xuất báo cáo bằng công cụ Allure với giao thiện thân thiện người dùng, giúp chúng ta dễ dàng phân tích. Quy trình kiểm thử được tích hợp vào CI/CD với công cụ Jenkins và nền tảng GitHub. Khi developer cập nhật mã nguồn và push lên GitHub, Jenkins sẽ tự động kích hoạt quá trình kiểm thử. Báo cáo kết quả kiểm thử được tạo tự động và gửi đến nhóm phát triển để họ có thể nhanh chóng xác định và xử lý các lỗi nếu có.