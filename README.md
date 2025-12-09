# Spring Magic Link Login

Ứng dụng Spring Boot + Spring Security sử dụng **One-Time Token (Magic Link)** để đăng nhập không cần mật khẩu.

## Tính năng
- Gửi Magic Link qua email (SendGrid)
- Dev mode: in link vào log nếu không có API key
- JTE template engine
- Trang `/home` sau khi login thành công

## Chạy thử

```bash
./mvnw spring-boot:run
