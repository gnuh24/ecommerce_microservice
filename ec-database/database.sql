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
    `title` 	VARCHAR(255) NOT NULL,
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

						INSERT INTO `Address` (`id`, `title`, `address`, `isDefault`, `isDeleted`, `fullName`, `phone`, `profileId`) VALUES
						('addr1', 'Nhà riêng', '123 Đường ABC, Quận 1, TP.HCM', TRUE, FALSE, 'Alice Nguyễn', '0909123456', 'P002'),
						('addr2', 'Công ty', '456 Đường DEF, Quận 3, TP.HCM', FALSE, FALSE, 'Alice Nguyễn', '0909123456', 'P002'),
						('addr3', 'Nhà', '789 Đường XYZ, Quận 10, TP.HCM', TRUE, FALSE, 'Bob Trần', '0909988776', 'P003');
