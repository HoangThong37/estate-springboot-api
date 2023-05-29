# estate-springboot-api
Xây dựng api website quản lý văn phòng cho thuê với logic cao áp dụng các best practice trong Java, Spring Framework gồm các công nghệ: Spring MVC, Spring Boot, Spring Security, Restful Web Service, Java 8, JPA, Spring Data JPA ...


## Chức năng tìm kiếm tòa nhà theo yêu cầu của khách hàng

- Tìm kiếm theo tên gần đúng : Tên tòa nhà, Phường, Đường, Hướng, Hạng...
- Tìm kiếm From_To   : Gía thuê phòng, Diện tích sàn..
- Tìm kiếm chính xác : Diện tích sàn, Số tầng hầm, Mã quận, Nhân viên quản lí tòa nhà, Loại tòa nhà,..

### Yêu cầu tìm kiếm một số chức năng
- Tìm kiếm địa chỉ : address = đường + phường + quận
- Tìm kiếm theo nhân viên quản lí : staffid
- Tìm kiếm theo quận : district_code 
-  Tìm kiếm diện tích thuê : tìm theo giá trị or tìm theo mảng [100,200]
- .....

## Chức năng tìm kiếm đã được update thêm các cách khác nhau

| Cách sử dụng | Chi tiết (Kết quả đạt được) |
|--------------|:------:-------------------|
| Code thuần ban đầu   | Chạy được, code dài dòng, không tối ưu |
| Áp dụng Map  | chạy tốt, code tối ưu hơn, build được hàm MapUtils, dùng Jdbc nên số lượng code vẫn lớn |
| Áp dụng Buider, jpa..  | chạy tốt, code tối ưu, ngắn gọn hơn, học được thêm kĩ thuật mới |








