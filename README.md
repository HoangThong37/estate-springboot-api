# estate-springboot-api
Xây dựng api website quản lý văn phòng cho thuê với logic cao áp dụng các best practice trong Java, Spring Framework gồm các công nghệ: Spring MVC, Spring Boot, Spring Security, Restful Web Service, Java 8, JPA, Spring Data JPA ...


Thực hiện chức năng tìm kiếm tòa nhà theo yêu cầu của khách hàng
+ Tìm kiếm theo field gần đúng : Tên tòa nhà, Phường, Đường, Hướng, Hạng
+ Tìm kiếm from_to   : Gía thuê phòng, Diện tích sàn
+ Tìm kiếm chính xác : Diện tích sàn, Số tầng hầm, Quận

Note: Tìm kiếm quận    : Nhập id và hiển thị  
      Tìm kiếm địa chỉ : address = đường + phường + quận
      Tìm kiếm theo nhân viên quản lí :   