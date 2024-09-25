-- xóa database
drop database tour_musketeers
go

-- tạo database
create database tour_musketeers
go

-- sử dụng database
use tour_musketeers
go

-- Tạo bảng users
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(255) NOT NULL,
    ho_ten NVARCHAR(255) NOT NULL,
    password NVARCHAR(255) NOT NULL,
    gioi_tinh NVARCHAR(50),
    sdt NVARCHAR(50),
    email NVARCHAR(255),
    dia_chi NVARCHAR(500),
    role INT
);

-- Tạo bảng Destination
CREATE TABLE destination (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    country NVARCHAR(255) NOT NULL
);

-- Tạo bảng Tour
CREATE TABLE tour (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_tour NVARCHAR(255) NOT NULL,
    gioi_thieu_tour NVARCHAR(MAX),
    so_ngay INT,
    noi_dung_tour NVARCHAR(MAX),
    ngay_khoi_hanh DATE,
    ngay_ket_thuc DATE,
    diem_den NVARCHAR(255),
    loai_tour INT,
    anh_tour NVARCHAR(255),
    diem_khoi_hanh NVARCHAR(255),
    trang_thai INT,
    gia_tour BIGINT
);

-- Tạo bảng Image
CREATE TABLE image (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    url NVARCHAR(500) NOT NULL,
    tour_id BIGINT NOT NULL,
    CONSTRAINT FK_Image_Tour FOREIGN KEY (tour_id) REFERENCES Tour(id)
);

-- Tạo bảng TourStart
CREATE TABLE tour_start (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    ngay_khoi_hanh DATE NOT NULL,
    tour_id BIGINT NOT NULL,
    CONSTRAINT FK_TourStart_Tour FOREIGN KEY (tour_id) REFERENCES Tour(id)
);

-- Tạo bảng Booking
CREATE TABLE booking (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tour_id BIGINT NOT NULL,
    so_luong_nguoi INT,
    ngay_khoi_hanh DATE,
    tong_tien BIGINT,
    trang_thai INT,
    booking_at DATETIME NOT NULL DEFAULT GETDATE(),
    pt_thanh_toan INT,
    ghi_chu NVARCHAR(MAX),
    CONSTRAINT FK_Booking_User FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FK_Booking_Tour FOREIGN KEY (tour_id) REFERENCES Tour(id)
);

-- Tạo bảng tin_tuc
CREATE TABLE tin_tuc (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    tieu_de NVARCHAR(255) NOT NULL,
    tom_tat NVARCHAR(MAX) NULL,
    noi_dung NVARCHAR(MAX) NULL,
    hinh_anh NVARCHAR(500) NULL,
    ngay_dang DATETIME2(6) NOT NULL
);

-- Chèn dữ liệu mẫu vào bảng [user]
INSERT INTO users (username, ho_ten, password, gioi_tinh, sdt, email, dia_chi, role)
VALUES 
('john_doe', 'John Doe', 'hashed_password1', N'Nam', '0909123456', 'john.doe@example.com', N'123 Đường ABC, Quận 1, TP.HCM', 1),
('jane_smith', 'Jane Smith', 'hashed_password2', N'Nữ', '0909234567', 'jane.smith@example.com', N'456 Đường DEF, Quận 3, TP.HCM', 2),
('admin_user', 'Admin User', 'hashed_password3', N'Nam', '0909345678', 'admin@example.com', N'789 Đường GHI, Quận 5, TP.HCM', 0);

-- Chèn dữ liệu mẫu vào bảng Destination
INSERT INTO destination (name, country)
VALUES 
(N'Hạ Long Bay', N'Việt Nam'),
(N'Bali', N'Indonesia'),
(N'Tokyo', N'Nhật Bản'),
(N'Paris', N'Pháp');

-- Chèn dữ liệu mẫu vào bảng Tour
INSERT INTO tour (ten_tour, gioi_thieu_tour, so_ngay, noi_dung_tour, ngay_khoi_hanh, ngay_ket_thuc, diem_den, loai_tour, anh_tour, diem_khoi_hanh, trang_thai, gia_tour)
VALUES 
(N'Tour Hạ Long 3 Ngày 2 Đêm', 'Tham quan vịnh Hạ Long tuyệt đẹp', 3, N'Chi tiết về tour Hạ Long...', '2024-12-01', '2024-12-03', N'Hạ Long Bay', 1, 'https://example.com/images/halong.jpg', N'TP.HCM', 1, 5000000),
(N'Tour Bali Thư Giãn 5 Ngày 4 Đêm', 'Trải nghiệm kỳ nghỉ tại Bali', 5, N'Chi tiết về tour Bali...', '2024-12-10', '2024-12-14', N'Bali', 2, 'https://example.com/images/bali.jpg', N'Hà Nội', 1, 15000000),
(N'Tour Tokyo Kỳ Thú 7 Ngày 6 Đêm', 'Khám phá văn hóa và công nghệ của Tokyo', 7, N'Chi tiết về tour Tokyo...', '2024-12-15', '2024-12-21', N'Tokyo', 3, 'https://example.com/images/tokyo.jpg', N'Đà Nẵng', 1, 25000000),
(N'Tour Paris Lãng Mạn 4 Ngày 3 Đêm', 'Trải nghiệm vẻ đẹp của Paris', 4, N'Chi tiết về tour Paris...', '2024-12-20', '2024-12-23', N'Paris', 4, 'https://example.com/images/paris.jpg', N'TP.HCM', 1, 20000000);

-- Chèn dữ liệu mẫu vào bảng Image
INSERT INTO image (url, tour_id)
VALUES 
('https://example.com/images/halong1.jpg', 1),
('https://example.com/images/halong2.jpg', 1),
('https://example.com/images/bali1.jpg', 2),
('https://example.com/images/bali2.jpg', 2),
('https://example.com/images/tokyo1.jpg', 3),
('https://example.com/images/tokyo2.jpg', 3),
('https://example.com/images/paris1.jpg', 4),
('https://example.com/images/paris2.jpg', 4);

-- Chèn dữ liệu mẫu vào bảng TourStart
INSERT INTO tour_start (ngay_khoi_hanh, tour_id)
VALUES 
('2024-12-01', 1),
('2024-12-10', 2),
('2024-12-15', 3),
('2024-12-20', 4),
('2025-01-05', 1),
('2025-01-15', 2);

-- Chèn dữ liệu mẫu vào bảng Booking
INSERT INTO booking (user_id, tour_id, so_luong_nguoi, ngay_khoi_hanh, tong_tien, trang_thai, pt_thanh_toan, ghi_chu)
VALUES 
(1, 1, 2, '2024-12-01', 10000000, 1, 1, N'Đặt cọc 50%, thanh toán còn lại trước ngày khởi hành'),
(2, 2, 4, '2024-12-10', 60000000, 1, 2, N'Thanh toán đầy đủ trước 2 tuần'),
(3, 3, 1, '2024-12-15', 25000000, 2, 1, N'Đã thanh toán đầy đủ'),
(1, 4, 3, '2024-12-20', 60000000, 1, 1, N'Đặt cọc 30%, thanh toán còn lại trước ngày khởi hành');

-- Chèn dữ liệu mẫu vào bảng tin_tuc
INSERT INTO tin_tuc (tieu_de, tom_tat, noi_dung, hinh_anh, ngay_dang)
VALUES 
(N'Khuyến Mãi Mùa Đông', N'Giảm giá lên đến 50% cho tất cả các tour mùa đông.', N'Chúng tôi hân hạnh thông báo chương trình khuyến mãi mùa đông với ưu đãi giảm giá lên đến 50% cho tất cả các tour du lịch...', 'https://example.com/images/khuyen_mai_mua_dong.jpg', '2024-11-15 10:30:00'),
(N'Tour Hạ Long Mới', N'Tham quan các điểm mới tại Vịnh Hạ Long.', N'Tour Hạ Long mới của chúng tôi bao gồm việc tham quan các điểm mới như...', 'https://example.com/images/tour_halong_moi.jpg', '2024-12-01 09:00:00'),
(N'Khám Phá Bali', N'Trải nghiệm kỳ nghỉ tuyệt vời tại Bali.', N'Bali không chỉ nổi tiếng với bãi biển mà còn với văn hóa độc đáo...', 'https://example.com/images/kham_pha_bali.jpg', '2024-12-05 14:45:00');

-- Kiểm tra dữ liệu trong bảng users
SELECT * FROM users;

-- Kiểm tra dữ liệu trong bảng Destination
SELECT * FROM destination;

-- Kiểm tra dữ liệu trong bảng Tour
SELECT * FROM tour;

-- Kiểm tra dữ liệu trong bảng Image
SELECT * FROM image;

-- Kiểm tra dữ liệu trong bảng TourStart
SELECT * FROM tour_start;

-- Kiểm tra dữ liệu trong bảng Booking
SELECT * FROM booking;
-- Kiểm tra bảng tin_tuc
SELECT * FROM tin_tuc;

select u1_0.id,
		u1_0.dia_chi,
		u1_0.email,
		u1_0.gioi_tinh,
		u1_0.ho_ten,
		u1_0.password,
		u1_0.role,
		u1_0.sdt,
		u1_0.username 
from users u1_0 where u1_0.username='john_doe'
