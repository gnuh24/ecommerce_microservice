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
    `createdAt` 	DATETIME 				NOT NULL,
    `updatedAt` 	DATETIME 				NOT NULL,
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


