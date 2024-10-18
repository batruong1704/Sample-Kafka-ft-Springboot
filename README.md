# Detail task
## 1. Hướng dẫn sử dụng:
1. Chạy file docker compose:
```cmd
docker compose up --build -d
```

2. Khởi động các service

3. Tạo bộ test, sử dụng postman với url
```cmd
http://localhost:8080/account/login
```

với bộ test
```json
{
    "username": "admin",
    "password": "admin"
}
```

## 2. Yêu cầu:

- Triển khai dưới dạng Master-Slave
- Xây dựng 3 service, 1 con tạo request lưu vào db, 1 con hiển thị lịch sử, 1 con hiển thị thông báo

## 3. Cấu trúc dự án:

Tạo 4 microservice và triển khai trên docker:
- Auth Service (Master): Xử lý đăng nhập và phân phối sự kiện.
    - Ở đây gồm 3 topic là saverecord, notification và history
    
- Database Service (Slave 1): Lưu thông tin vào cơ sở dữ liệu. 
- History Service (Slave 2): Hiển thị lịch sử đăng nhập. 

INPUT: API login gồm username và pw
OUTPUT:
- service 1: nhận api và xác thực, phân phối cho các consumer
- service 2: lưu data là lịch sử đăng nhập vào db
- service 3: hiển thị data xuống console và lưu vào file logs

1. Auth Service (producer):
   - Xử lý đăng nhập và xác thực người dùng.
   - Tạo và gửi các sự kiện Kafka khi có hoạt động đăng nhập.

2. Database Service (consumer):
   - Lắng nghe các sự kiện từ Kafka và lưu thông tin vào cơ sở dữ liệu.
   - Không có API công khai, chỉ xử lý dữ liệu nội bộ.

3. History Service (consumer):
   - Lắng nghe và hiển thị lịch sử hoạt động để ghi xuống console và file log