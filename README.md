# Detail task
## 1. Yêu cầu:

- Triển khai dưới dạng Master-Slave
- Xây dựng 3 service, 1 con tạo request lưu vào db, 1 con hiển thị lịch sử, 1 con hiển thị thông báo

## 2. Cấu trúc dự án:

Tạo 4 microservice và triển khai trên docker:
- Auth Service (Master): Xử lý đăng nhập và phân phối sự kiện.
    - Ở đây gồm 3 topic là saverecord, notifi
- Database Service (Slave 1): Lưu thông tin vào cơ sở dữ liệu. 
- History Service (Slave 2): Hiển thị lịch sử đăng nhập. 
- Notification Service (Slave 3): Gửi thông báo.

```plaintext
project-root/
├── auth-service/
│   ├── src/main/java/com/yourcompany/authservice/
│   │   ├── AuthServiceApplication.java
│   │   ├── controller/
│   │   │   └── AuthController.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   └── KafkaProducerService.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   └── LoginEvent.java
│   │   └── config/
│   │       └── KafkaConfig.java
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── pom.xml
│   └── Dockerfile

├── database-service/
│   ├── src/main/java/com/yourcompany/dbservice/
│   │   ├── DatabaseServiceApplication.java
│   │   ├── service/
│   │   │   ├── KafkaConsumerService.java
│   │   │   └── DatabaseService.java
│   │   ├── model/
│   │   │   └── LoginRecord.java
│   │   ├── repository/
│   │   │   └── LoginRecordRepository.java
│   │   └── config/
│   │       ├── KafkaConfig.java
│   │       └── DatabaseConfig.java
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── pom.xml
│   └── Dockerfile

├── history-service/
│   ├── src/main/java/com/yourcompany/historyservice/
│   │   ├── HistoryServiceApplication.java
│   │   ├── controller/
│   │   │   └── HistoryController.java
│   │   ├── service/
│   │   │   └── HistoryService.java
│   │   ├── model/
│   │   │   └── LoginHistory.java
│   │   └── repository/
│   │       └── LoginHistoryRepository.java
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── pom.xml
│   └── Dockerfile

├── notification-service/
│   ├── src/main/java/com/yourcompany/notificationservice/
│   │   ├── NotificationServiceApplication.java
│   │   ├── service/
│   │   │   ├── KafkaConsumerService.java
│   │   │   └── NotificationService.java
│   │   ├── model/
│   │   │   └── Notification.java
│   │   └── config/
│   │       └── KafkaConfig.java
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── pom.xml
│   └── Dockerfile

└── docker-compose.yml

```

## 3. Chi tiết:

INPUT: API login gồm username và pw
OUTPUT:
- service 1: nhận api và xác thực, phân phối cho các consumer
- service 2: lưu data là lịch sử đăng nhập vào db
- service 3: ??


1. Auth Service (producer):
   - Xử lý đăng nhập và xác thực người dùng.
   - Tạo và gửi các sự kiện Kafka khi có hoạt động đăng nhập.

2. Database Service (consumer):
   - Lắng nghe các sự kiện từ Kafka và lưu thông tin vào cơ sở dữ liệu.
   - Không có API công khai, chỉ xử lý dữ liệu nội bộ.

3. History Service (consumer):
   - Lắng nghe và hiển thị lịch sử hoạt động, theo dõi ở log.

4. Notification Service (consumer):
   - Lắng nghe các sự kiện từ Kafka và gửi thông báo tương ứng (email, push notification, SMS, v.v.).

Giải thích chi tiết:

1. Auth Service:
   - `AuthController`: Xử lý yêu cầu đăng nhập.
   - `AuthService`: Xác thực người dùng.
   - `KafkaProducerService`: Gửi sự kiện đăng nhập tới Kafka.
   - `LoginEvent`: Model cho sự kiện đăng nhập.

2. Database Service:
   - `KafkaConsumerService`: Lắng nghe sự kiện đăng nhập từ Kafka.
   - `DatabaseService`: Xử lý logic lưu trữ dữ liệu.
   - `LoginRecordRepository`: Interface để tương tác với cơ sở dữ liệu.

3. History Service:
   - `HistoryController`: Cung cấp API để truy vấn lịch sử đăng nhập.
   - `HistoryService`: Xử lý logic truy xuất lịch sử.
   - `LoginHistoryRepository`: Interface để truy vấn dữ liệu lịch sử.

4. Notification Service:
   - `KafkaConsumerService`: Lắng nghe sự kiện đăng nhập từ Kafka.
   - `NotificationService`: Xử lý logic gửi thông báo.


Luồng hoạt động:
1. Người dùng đăng nhập qua Auth Service.
2. Auth Service xác thực và gửi sự kiện đăng nhập tới Kafka.
3. Database Service nhận sự kiện và lưu vào cơ sở dữ liệu.
4. History Service có thể truy vấn dữ liệu này để hiển thị lịch sử.
5. Notification Service nhận sự kiện và gửi thông báo.

Cấu trúc này phản ánh tốt hơn chức năng cụ thể của từng service như bạn đã mô tả. Mỗi service tập trung vào một nhiệm vụ cụ thể, tuân theo nguyên tắc "Single Responsibility" của thiết kế microservices.

Bạn có muốn đi sâu vào chi tiết triển khai của bất kỳ service nào không?"