# BKCare Backend
Backend của ứng dụng BKCare, môn LTNC HK232

# Cài đặt
Lưu ý: phải có JRE 21
Tải file app.jar từ https://github.com/chezzijr/hospital-manager-backend/releases
Chạy: `java -jar app.jar`

# Cài đặt từ mã nguồn
Cần phải có JDK, JRE 21, Gradle 8.6, Makefile
- Tạo tài khoản và project Firebase
- Generate new private key => tải về, đổi tên thành `firebase-sdk.json` và đặt vào `app/src/main/resources`
- Trong directory `app/src/main/resources` tạo file `secrets.properties` có nội dung `firebase.web.api.key=<API-KEY>` trong đó API-KEY là Web Api Key của Firebase project
- Chạy `make build` để build, file `.jar` sẽ được đặt ở `app/build/libs`
- Chạy `app.jar`

# Sử dụng docker
- Pull từ https://hub.docker.com/r/chezzijr/bkcare-be
