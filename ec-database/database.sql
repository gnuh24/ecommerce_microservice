DROP DATABASE IF EXISTS `EC_Microservice`;
CREATE DATABASE `EC_Microservice`;
USE `EC_Microservice`;

CREATE TABLE `Profile` (
    `id` 		VARCHAR(255) 	PRIMARY KEY,
    `email` 	VARCHAR(255) 	NOT NULL 	UNIQUE,
    `phone` 	VARCHAR(255)  				UNIQUE,
    `fullName` 	VARCHAR(255) ,
    `birthday` 	DATE,
    `gender` 	ENUM("MALE", "FEMALE", "ORTHER")
);

CREATE TABLE `Account` (
    `id` 			VARCHAR(255) 			PRIMARY KEY,
    `createdAt` 	TIMESTAMP 				NOT NULL,
    `updatedAt` 	TIMESTAMP 				NOT NULL,
    `username` 		VARCHAR(255) 			NOT NULL UNIQUE,
    `password` 		VARCHAR(255) 			NOT NULL,
    `role` 			ENUM('ADMIN', 'USER') 	NOT NULL,
    `status` 		ENUM('ACTIVE', 'INACTIVE', 'BANNED') NOT NULL,
    `profileId` 	VARCHAR(255) NOT NULL,
     FOREIGN KEY (`profileId`) REFERENCES `Profile`(`id`)
);

CREATE TABLE `Address` (
    `id` 		VARCHAR(255) PRIMARY KEY,
    `address` 	VARCHAR(255) NOT NULL,
    `isDefault` BOOLEAN NOT NULL,
    `isDeleted` BOOLEAN NOT NULL,
    `fullName` 	VARCHAR(255) NOT NULL,
    `phone` 	VARCHAR(255) NOT NULL,
    `profileId` VARCHAR(255) NOT NULL,
     FOREIGN KEY (`profileId`) REFERENCES `Profile`(`id`)
);

INSERT INTO `Profile`   (`id`, 		`email`, 			`phone`, 	`fullName`, 		`birthday`, `gender`) VALUES
						('P001', 	'admin@gmail.com', '0909123456', 'Ngô Tuấn Hưng', 	'2004-04-02', 'MALE'),
						('P002', 	'user1@gmail.com', '0909988776', 'Bob Trần', 		'1992-03-22', 'FEMALE'),
						('P003', 	'user2@gmail.com', '0911223344', 'Charlie Phạm', 	NULL, NULL);
                        
INSERT INTO `Account` 	(`id`, 		`createdAt`, `updatedAt`, `username`, `password`, `role`, `status`,  `profileId`) VALUES
						('acc1', 	NOW(), 			NOW(), 'admin@gmail.com', '$2a$10$W2neF9.6Agi6kAKVq8q3fec5dHW8KUA.b0VSIGdIZyUravfLpyIFi', 'ADMIN', 'ACTIVE', 'P001'),
						('acc2', 	NOW(), 			NOW(), 'user1@gmail.com', '$2a$10$W2neF9.6Agi6kAKVq8q3fec5dHW8KUA.b0VSIGdIZyUravfLpyIFi', 'USER', 'ACTIVE', 'P002'),
						('acc3', 	NOW(), 			NOW(), 'user2@gmail.com', '$2a$10$W2neF9.6Agi6kAKVq8q3fec5dHW8KUA.b0VSIGdIZyUravfLpyIFi', 'USER', 'INACTIVE',  'P003');

-- Dữ liệu cho tài khoản P002
INSERT INTO `Address` (`id`, `address`, `isDefault`, `isDeleted`, `fullName`, `phone`, `profileId`) VALUES
('A001', '123 Lê Lợi, Q.1, TP.HCM',       true,  false, 'Nguyễn Văn A', '0909123456', 'P002'),
('A002', '456 Nguyễn Trãi, Q.5, TP.HCM',   false, false, 'Nguyễn Văn A', '0909123456', 'P002'),
('A003', '789 Cách Mạng Tháng 8, Q.10',    false, false,  'Nguyễn Văn A', '0909123456', 'P002'),
('A004', '12 Phan Xích Long, Q. Phú Nhuận',false, false, 'Nguyễn Văn A', '0909123456', 'P002'),
('A005', '34 Trường Chinh, Q. Tân Bình',   false, true,  'Nguyễn Văn A', '0909123456', 'P002');







 -- ___________________________________________________________ CATALOG SERVICE ______________________________________________________________
 
CREATE TABLE `Category` (
    `id` 			VARCHAR(10) PRIMARY KEY,
    `categoryName` 			VARCHAR(100) NOT NULL UNIQUE,
    `productCount` 	INT DEFAULT 0,
    
    `createdAt` 	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` 	TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deletedAt` 	TIMESTAMP NULL DEFAULT NULL,
    `isDeleted` 	BOOLEAN DEFAULT FALSE
);

INSERT INTO `Category` (`id`, `categoryName`) VALUES
('C001', 'Loại sản phẩm khác'),
('C002', 'Whisky'),
('C003', 'Vodka'),
('C004', 'Rum'),
('C005', 'Tequila'),
('C006', 'Brandy'),
('C007', 'Gin'),
('C008', 'Champagne'),
('C009', 'Wine'),
('C010', 'Sake'),
('C011', 'Cognac');


CREATE TABLE `Brand` (
    `id`            VARCHAR(10) PRIMARY KEY,
    `brandName`          VARCHAR(100) NOT NULL UNIQUE,
    `productCount`  INT DEFAULT 0,
    
    `createdAt`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deletedAt`     TIMESTAMP NULL DEFAULT NULL,
    `isDeleted`     BOOLEAN DEFAULT FALSE
);

INSERT INTO `Brand` (`id`, `brandName`) VALUES
('B001', 'Thương hiệu khác'),
('B002', 'Johnnie Walker'),
('B003', 'Absolut'),
('B004', 'Bacardi'),
('B005', 'Patrón'),
('B006', 'Hennessy'),
('B007', 'Tanqueray'),
('B008', 'Moët & Chandon'),
('B009', 'Château Margaux'),
('B010', 'Suntory'),
('B011', 'Remy Martin');



CREATE TABLE `Product` (
    `id`            VARCHAR(10) PRIMARY KEY,
    `name`          VARCHAR(255) NOT NULL,
    `slug`          VARCHAR(255) NOT NULL UNIQUE,
    `description`   TEXT,
    `vintage`       YEAR,
    `alcohol`       DECIMAL(5,2),
    `region`        VARCHAR(100),
    `isPublished`   BOOLEAN DEFAULT FALSE,
    `createdAt`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updatedAt`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deletedAt`     TIMESTAMP NULL DEFAULT NULL,
    `isDeleted`     BOOLEAN DEFAULT FALSE,
    `categoryId`    VARCHAR(10) NOT NULL,
    `brandId`       VARCHAR(10) NOT NULL,
    FOREIGN KEY (`categoryId`) REFERENCES `Category`(`id`),
    FOREIGN KEY (`brandId`) REFERENCES `Brand`(`id`)
);


INSERT INTO `Product` (
    `id`, `name`, `slug`, `description`, `vintage`, `alcohol`, `region`, `isPublished`,
    `createdAt`, `updatedAt`, `isDeleted`, `deletedAt`, `categoryId`, `brandId`
) VALUES
('P001', 'Johnnie Walker Blue Label', 'johnnie-walker-blue-label', 'Dòng whisky thượng hạng với hương vị đậm đà và mượt mà.', 2020, 40.00, 'Scotland', TRUE, NOW(), NOW(), FALSE, NULL, 'C002', 'B002'),

('P002', 'Absolut Vodka Original', 'absolut-vodka-original', 'Vodka Thụy Điển nguyên chất, không có chất phụ gia.', 2022, 40.00, 'Sweden', TRUE, NOW(), NOW(), FALSE, NULL, 'C003', 'B003'),

('P003', 'Hennessy VSOP', 'hennessy-vsop', 'Cognac Pháp nổi tiếng với hương thơm trái cây và vị cay nhẹ.', 2019, 40.00, 'France', TRUE, NOW(), NOW(), FALSE, NULL, 'C011', 'B006'),

('P004', 'Moët & Chandon Brut Impérial', 'moet-chandon-brut-imperial', 'Champagne nổi tiếng đến từ Pháp, mang phong cách tươi mới.', 2021, 12.00, 'France', TRUE, NOW(), NOW(), FALSE, NULL, 'C008', 'B008'),

('P005', 'Château Margaux Grand Vin', 'chateau-margaux-grand-vin', 'Rượu vang đỏ cao cấp vùng Bordeaux, đậm đà và phức tạp.', 2018, 13.50, 'France - Bordeaux', TRUE, NOW(), NOW(), FALSE, NULL, 'C009', 'B009'),

('P006', 'Suntory Hibiki Harmony', 'suntory-hibiki-harmony', 'Whisky Nhật Bản pha trộn tinh tế giữa truyền thống và hiện đại.', 2020, 43.00, 'Japan', TRUE, NOW(), NOW(), FALSE, NULL, 'C002', 'B010'),

('P007', 'Patrón Silver Tequila', 'patron-silver-tequila', 'Tequila Mexico tinh khiết, thích hợp để uống nguyên chất hoặc pha chế.', 2021, 40.00, 'Mexico', TRUE, NOW(), NOW(), FALSE, NULL, 'C005', 'B005'),

('P008', 'Tanqueray London Dry Gin', 'tanqueray-london-dry-gin', 'Gin nổi tiếng với hương vị mạnh mẽ và thanh thoát.', 2021, 47.30, 'England', TRUE, NOW(), NOW(), FALSE, NULL, 'C007', 'B007'),

('P009', 'Remy Martin XO', 'remy-martin-xo', 'Dòng cognac hảo hạng với hương thơm trái cây khô và vani.', 2017, 40.00, 'France - Cognac', TRUE, NOW(), NOW(), FALSE, NULL, 'C011', 'B011'),

('P010', 'Rượu vang Đà Lạt Classic', 'ruou-vang-da-lat-classic', 'Rượu vang đỏ sản xuất tại Việt Nam, thích hợp dùng hằng ngày.', 2022, 12.50, 'Vietnam - Đà Lạt', TRUE, NOW(), NOW(), FALSE, NULL, 'C009', 'B001');


CREATE TABLE `ProductVariant` (
    `id` 			VARCHAR(10) PRIMARY KEY,
    `volume` 		INT NOT NULL, -- đơn vị ml, immutable
    `price` 		DECIMAL(12,2) NOT NULL,
    `quantity` 		INT NOT NULL DEFAULT 0,
    `isPublished` 	BOOLEAN DEFAULT FALSE,
    `isDeleted` 	BOOLEAN DEFAULT FALSE,
    `createdAt` 	DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updatedAt` 	DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deletedAt` 	DATETIME DEFAULT NULL,
    
	`productId` 	VARCHAR(10) NOT NULL,
    FOREIGN KEY (`productId`) REFERENCES `Product`(`id`)
);

INSERT INTO `ProductVariant` (
    `id`, `productId`, `volume`, `price`, `quantity`,
    `isPublished`, `isDeleted`, `createdAt`, `updatedAt`, `deletedAt`
) VALUES
('V001', 'P001', 700, 4500000, 20, TRUE, FALSE, NOW(), NOW(), NULL),
('V002', 'P002', 750, 400000, 100, TRUE, FALSE, NOW(), NOW(), NULL),
('V003', 'P003', 700, 2300000, 30, TRUE, FALSE, NOW(), NOW(), NULL),
('V004', 'P004', 750, 1800000, 50, TRUE, FALSE, NOW(), NOW(), NULL),
('V005', 'P005', 750, 5200000, 10, TRUE, FALSE, NOW(), NOW(), NULL),
('V006', 'P006', 700, 3200000, 25, TRUE, FALSE, NOW(), NOW(), NULL),
('V007', 'P007', 700, 1600000, 70, TRUE, FALSE, NOW(), NOW(), NULL),
('V008', 'P008', 700, 1400000, 40, TRUE, FALSE, NOW(), NOW(), NULL),
('V009', 'P009', 700, 3900000, 15, TRUE, FALSE, NOW(), NOW(), NULL),
('V010', 'P010', 750, 280000, 120, TRUE, FALSE, NOW(), NOW(), NULL);


CREATE TABLE `ProductImage` (
    `id` VARCHAR(10) PRIMARY KEY,
    `productId` VARCHAR(10) NOT NULL,
    `imageUrl` VARCHAR(1024) NOT NULL,
    `isThumbnail` BOOLEAN DEFAULT FALSE,
    `isDeleted` BOOLEAN DEFAULT FALSE,
    `createdAt` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deletedAt` DATETIME DEFAULT NULL,
    FOREIGN KEY (`productId`) REFERENCES `Product`(`id`)
);


INSERT INTO `ProductImage` (
    `id`, `productId`, `imageUrl`, `isThumbnail`, `isDeleted`, `createdAt`, `deletedAt`
) VALUES
('IMG001', 'P001', 'https://example.com/images/p001.jpg', TRUE, FALSE, NOW(), NULL),
('IMG002', 'P002', 'https://example.com/images/p002.jpg', TRUE, FALSE, NOW(), NULL),
('IMG003', 'P003', 'https://example.com/images/p003.jpg', TRUE, FALSE, NOW(), NULL),
('IMG004', 'P004', 'https://example.com/images/p004.jpg', TRUE, FALSE, NOW(), NULL),
('IMG005', 'P005', 'https://example.com/images/p005.jpg', TRUE, FALSE, NOW(), NULL),
('IMG006', 'P006', 'https://example.com/images/p006.jpg', TRUE, FALSE, NOW(), NULL),
('IMG007', 'P007', 'https://example.com/images/p007.jpg', TRUE, FALSE, NOW(), NULL),
('IMG008', 'P008', 'https://example.com/images/p008.jpg', TRUE, FALSE, NOW(), NULL),
('IMG009', 'P009', 'https://example.com/images/p009.jpg', TRUE, FALSE, NOW(), NULL),
('IMG010', 'P010', 'https://example.com/images/p010.jpg', TRUE, FALSE, NOW(), NULL);











