
-- DROP DATABASE IF EXISTS gmdb;

-- CREATE DATABASE gmdb;

-- USE gmdb;
USE heroku_7662f34ad93a4a5;

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'user id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `email` varchar(200) DEFAULT NULL COMMENT 'Email',
  `password` varchar(60) NOT NULL COMMENT 'Password',
  `username` varchar(200) NOT NULL COMMENT 'Username',
  `avatar` varchar(200) DEFAULT NULL COMMENT 'User avatar',
  `account_enabled` bit(1) DEFAULT NULL COMMENT 'Account status',
  `notification_enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT 'Notification status',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` (`id`, `add_time`, `update_time`, `email`, `password`, `username`, `avatar`, `account_enabled`, `notification_enabled`) VALUES
	(1, '2021-07-06 14:38:54', '2021-07-06 14:38:54', '123456@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Jie', 'https://i.ibb.co/Zdy0ZmY/subsource-done-button-uid-6935-E5-D1-95-F9-459-A-ADA3-1368160971-F1-1588000115493-source-other-origi.jpg', b'1', b'1'),
	(2, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'test@qq.com', '$2a$10$0Qg7/cQhkFZw2MCFOVIz/O12YhZ/YT.ReKPnVQQD9R8/ApAcq2vcC', 'test', 'https://qiniu.easyapi.com/user/default.jpg', b'1', b'1'),
	(3, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user1@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Wildicute', 'https://i.ibb.co/g7bpR4c/IMG-3455.jpg', b'1', b'1'),
	(4, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user2@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Childintime', 'https://i.ibb.co/FXJLyrc/IMG-3456.jpg', b'1', b'1'),
	(5, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user3@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Vihoo', 'https://i.ibb.co/kHjQNDM/IMG-3457.jpg', b'1', b'1'),
	(6, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user4@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Psychox', 'https://i.ibb.co/L8fZ5vY/IMG-3458.jpg', b'1', b'1'),
	(7, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user5@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Blazeuu', 'https://i.ibb.co/mRP83zL/IMG-3459.jpg', b'1', b'1'),
	(8, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user6@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Sparklen', 'https://i.ibb.co/zRF3xbw/IMG-3461.jpg', b'1', b'1'),
	(9, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user7@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Aristêo', 'https://i.ibb.co/YkNwZmF/IMG-3460.jpg', b'1', b'1'),
	(10, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user8@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Thaner', 'https://i.ibb.co/rpMrQd4/IMG-3462.jpg', b'1', b'1'),
	(11, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user9@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Lagbugdc', 'https://i.ibb.co/d58mGCt/IMG-3463.jpg', b'1', b'1'),
	(12, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user10@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Mòádmòád', 'https://i.ibb.co/S5Ffv7P/IMG-3464.jpg', b'1', b'1'),
	(13, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user11@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Ellesmêre', 'https://i.ibb.co/56kLnBP/IMG-3465.jpg', b'1', b'1'),
	(14, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user12@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Cptrogers', 'https://i.ibb.co/8ryp9bH/IMG-3466.jpg', b'1', b'1'),
	(15, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user13@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Skylarkedd', 'https://i.ibb.co/pfjwgMC/IMG-3467.jpg', b'1', b'1'),
	(16, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user14@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Rare', 'https://i.ibb.co/588SfNR/IMG-3468.jpg', b'1', b'1'),
	(17, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user15@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Palbrew', 'https://i.ibb.co/m6PYQdT/IMG-3469.jpg', b'1', b'1'),
	(18, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user16@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Eltharièn', 'https://i.ibb.co/RBsGrKS/IMG-3470.jpg', b'1', b'1'),
	(19, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user17@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Дирекшен', 'https://i.ibb.co/TMPkCMk/IMG-3471.jpg', b'1', b'1'),
	(20, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user18@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Kenefin', 'https://i.ibb.co/S5kNXLt/IMG-3472.jpg', b'1', b'1'),
	(21, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user19@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Gingi', 'https://i.ibb.co/W63xrS5/IMG-3473.jpg', b'1', b'1'),
	(22, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user20@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Rycntwo', 'https://i.ibb.co/VJCDCLP/IMG-3474.jpg', b'1', b'1'),
	(23, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user21@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Jpc', 'https://i.ibb.co/Jqhrwr7/IMG-3475.jpg', b'1', b'1'),
	(24, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user22@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Avade', 'https://i.ibb.co/vvPtZyc/IMG-3476.jpg', b'1', b'1'),
	(25, '2021-10-22 19:26:06', '2021-10-12 19:26:06', 'user23@gmail.com', '$2a$10$fA8SxTzDoN1cpKB8X4tbIOSP4zi2CDHmzI1whhb4/lgfwpMyLh/XO', 'Fínís', 'https://i.ibb.co/n8zb4Vn/IMG-3477.jpg', b'1', b'1'),
	(35, '2021-11-05 12:32:37', '2021-11-05 12:32:37', 'user27@gmail.com', '$2a$10$guCDyaOpgw.oBuugHHpt4OE2C75fqeuzxaZCqOWULooTiV6zK9OW6', 'kaka', 'http://54.154.208.158/image/avatar/WTJbd9086c7-28fd-41d6-bc61-68c4133d00b3.png', b'1', b'1'),
	(45, '2021-11-05 23:11:53', '2021-11-05 23:11:53', 'user30@gmail.com', '$2a$10$aMxJeO1U0hGsF0aVMFIj8.1KynuCHo.EPttLKo1RMgvKUEa74aHMm', 'lalafas', 'http://54.154.208.158/image/avatar/WTJea594b5e-9f2a-4819-9eb8-7dabbaa599ce.png', b'1', b'1');

-- ----------------------------
-- Table structure for category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `category` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'Category id',
    `add_time` DATETIME DEFAULT NULL COMMENT 'Add time',
    `update_time` DATETIME DEFAULT NULL COMMENT 'Update time',
    `name` VARCHAR(255) DEFAULT NULL COMMENT 'Category name',
    PRIMARY KEY (`id`) USING BTREE
)  ENGINE=INNODB AUTO_INCREMENT=7 DEFAULT CHARSET=UTF8MB4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` (`id`, `add_time`, `update_time`, `name`) VALUES
	(1, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'General'),
	(2, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'News'),
	(3, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Guide'),
	(4, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Previews'),
	(5, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Reviews');
	-- (6, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Videos');

-- ----------------------------
-- Table structure for channels
-- ----------------------------
CREATE TABLE IF NOT EXISTS `channels` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'channels id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `name` varchar(255) DEFAULT NULL COMMENT 'channels name',
  `logo` varchar(255) DEFAULT NULL COMMENT 'channels logo',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `channels` (`id`, `add_time`, `update_time`, `name`, `logo`) VALUES
	(1, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Other', 'https://i.ibb.co/bL0CRfj/Unknown.png'),
	(2, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'League of Legends', 'https://i.ibb.co/7XNG4sD/lol.png'),
	(3, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'World of Warcraft', 'https://i.ibb.co/YhY14DR/wow.jpg'),
	(4, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Grand Theft Auto V', 'https://i.ibb.co/9G1WQgT/gta5.jpg'),
	(5, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Minecraft', 'https://i.ibb.co/5jN6gbS/minecraft.jpg'),
	(6, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Ace Combat7', 'https://i.ibb.co/XjpGFvR/Ace-Combat-7.jpg'),
	(7, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Cyberpunk2077', 'https://i.ibb.co/bWW3y83/Cyberpunk2077.jpg'),
	(8, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Diablo3', 'https://i.ibb.co/L5K0gPw/diablo3.jpg'),
	(9, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'DOTA2', 'https://i.ibb.co/HzR48Ly/Dota2.jpg'),
	(10, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Dragon Ball Fighter Z', 'https://i.ibb.co/hfZBJHS/dragon-ball-fighter-z.jpg'),
	(11, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'FFXIV', 'https://i.ibb.co/9Vc2XbP/FFXIV.jpg'),
	(12, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'FIFA 22', 'https://i.ibb.co/kGpjzZw/FIFA-22.jpg'),
	(13, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Far Cry', 'https://i.ibb.co/NpsGgK3/Far-Cry.jpg'),
	(14, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'God of War', 'https://i.ibb.co/BBsMyqC/god-of-war.jpg'),
	(15, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Fromsoftware', 'https://i.ibb.co/Gxf4DLM/Fromsoftware.jpg'),
	(16, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Hearthstone', 'https://i.ibb.co/23yxLdZ/Hearthstone.jpg'),
	(17, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Kena Bridge of Spirits', 'https://i.ibb.co/vXK1MvS/Kena-Bridge-of-Spirits.jpg'),
	(18, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Monster Hunter Rise', 'https://i.ibb.co/TczLSxm/monster-hunter-rise.jpg'),
	(19, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'New World', 'https://i.ibb.co/bW3xdtd/New-world.jpg'),
	(20, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'NieR Automata', 'https://i.ibb.co/hXkP3Yb/Nie-R-Automata.jpg'),
	(21, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Overwatch', 'https://i.ibb.co/qxSf22J/Overwatch.jpg'),
	(22, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Red Dead Redemption2', 'https://i.ibb.co/C7ryr25/Red-Dead-Redemption2.jpg'),
	(23, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Resident Evil Village', 'https://i.ibb.co/Kq0sxPw/Resident-Evil-Village.jpg'),
	(24, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Zelda', 'https://i.ibb.co/HrhCvCB/Zelda.jpg'),
	(25, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'The Witcher 3', 'https://i.ibb.co/bRxn8jH/The-witcher-3.jpg'),
	(26, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'The Last of Us', 'https://i.ibb.co/QnvgxwM/The-last-of-us.jpg'),
	(27, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Civilization VI', 'https://i.ibb.co/LPhmD2q/Sid-Meier-s-Civilization-VI.jpg'),
	(28, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Rise of The Tomb', 'https://i.ibb.co/XjTByCP/rise-of-the-tomb.jpg');

-- ----------------------------
-- Table structure for channels_category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `channels_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Channels category id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `name` varchar(255) DEFAULT NULL COMMENT 'Channels category name',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of channels_category
-- ----------------------------
INSERT INTO `channels_category` (`id`, `add_time`, `update_time`, `name`) VALUES
	(1, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'PC'),
	(2, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Xbox'),
	(3, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Nintendo'),
	(4, '2021-08-21 11:10:53', '2021-08-21 11:10:57', 'Playstation');

-- ----------------------------
-- Table structure for channels_game_categary
-- ----------------------------
CREATE TABLE IF NOT EXISTS `channels_game_categary` (
  `channels_id` int(11) NOT NULL COMMENT 'Channels id',
  `game_category_id` int(11) NOT NULL COMMENT 'Game category id',
  PRIMARY KEY (`channels_id`,`game_category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


-- ----------------------------
-- Records of channels_game_categary
-- ----------------------------
INSERT INTO `channels_game_categary` (`channels_id`, `game_category_id`) VALUES
	(2, 1), -- LOL pc
	(3, 1), -- WoW pc
    (4, 1), -- GTA5 pc
    (4, 2), -- GTA5 xobx
    (4, 4), -- GTA5 ps
    (5, 1), -- Minecarft pc
    (5, 2), -- Minecarft xbox
    (5, 3), -- Minecarft ns
    (5, 4), -- Minecarft ps
    (6, 1), -- Ace combat7 pc
    (6, 2), -- Ace combat7 xbox
    (6, 4), -- Ace combat7 ps
    (7, 1), -- Cyberpunk2077 pc
    (7, 2), -- Cyberpunk2077 xbox
    (7, 4), -- Cyberpunk2077 ps
    (8, 1), -- Diable3 pc
    (8, 2), -- Diable3 xbox
    (8, 3), -- Diable3 ns
    (8, 4), -- Diable3 ps
    (9, 1), -- DOTA2 pc
    (10, 1), -- Dragon ball fighter Z pc
    (10, 2), -- Dragon ball fighter Z xbox
    (10, 3), -- Dragon ball fighter Z ns
    (10, 4), -- Dragon ball fighter Z ps
    (11, 1), -- FF14 pc
    (11, 4), -- FF14 ps
    (12, 1), -- FIFA22 pc
    (12, 2), -- FIFA22 xbox
    (12, 4), -- FIFA22 ps
    (13, 1), -- Far Cry pc
    (13, 2), -- Far Cry xbox
    (13, 4), -- Far Cry ps
    (14, 1), -- God of war pc
    (14, 4), -- God of war ps
    (15, 1), -- Fromsoftware pc
    (15, 2), -- Fromsoftware xbox
    (15, 4), -- Fromsoftware ps
    (16, 1), -- Hearthstone pc
    (17, 1), -- Kena bridge pc
    (17, 4), -- Kena bridge ps
    (18, 3), -- Minster Hunter Rise ns
    (19, 1), -- New world pc
    (20, 1), -- NieR automata pc
    (20, 2), -- NieR automata xbox
    (20, 4), -- NieR automata ps
    (21, 1), -- Overwatch pc
    (21, 2), -- Overwatch xbox
    (21, 3), -- Overwatch ns
    (21, 4), -- Overwatch ps
    (22, 1), -- Red dead redemption2 pc
    (22, 2), -- Red dead redemption2 xbox
    (22, 4), -- Red dead redemption2 ns
    (23, 1), -- Resident evil village pc
    (23, 2), -- Resident evil village xbox
    (23, 4), -- Resident evil village ps
    (24, 3), -- Zelda ns
    (25, 1), -- The wintcher3 pc
    (25, 2), -- The wintcher3 xbox
    (25, 3), -- The wintcher3 ns
    (25, 4), -- The wintcher3 ps
    (26, 4), -- The last of us ps
    (27, 1), -- civilization 6 pc
    (27, 2), -- civilization 6 xbox
    (27, 3), -- civilization 6 ns
    (27, 4); -- civilization 6 ps
    
    
-- ----------------------------
-- Table structure for channels_history
-- ----------------------------
CREATE TABLE IF NOT EXISTS `channels_history` (
  `user_id` int(11) NOT NULL COMMENT 'User id',
  `channels_id` int(11) NOT NULL COMMENT 'Channels id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  PRIMARY KEY (`channels_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='User channel browsing history';

-- ----------------------------
-- Table structure for channels_subscription
-- ----------------------------
CREATE TABLE IF NOT EXISTS `channels_subscription` (
  `user_id` int(11) NOT NULL COMMENT 'User id',
  `channels_id` int(11) NOT NULL COMMENT 'Channels id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  PRIMARY KEY (`channels_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='User Channel Subscription';

-- ----------------------------
-- Table structure for collect
-- ----------------------------
CREATE TABLE IF NOT EXISTS `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Collect id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `collect_user_id` int(11) DEFAULT NULL COMMENT 'Follow user id',
  `content_id` int(11) DEFAULT NULL COMMENT 'coolect content id',
  `good_content_id` int(11) DEFAULT NULL COMMENT 'Liked content id',
  `user_id` int(11) DEFAULT NULL COMMENT 'User id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9125 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='All collection table';


-- ----------------------------
-- Table structure for comment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Comment id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  `content` varchar(500) DEFAULT NULL COMMENT 'Content',
  `enabled` bit(1) DEFAULT NULL COMMENT 'Availability status',
  `content_id` int(11) DEFAULT NULL COMMENT 'Content id',
  `user_id` int(11) NOT NULL COMMENT 'Comment user id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


-- ----------------------------
-- Table structure for content
-- ----------------------------
CREATE TABLE IF NOT EXISTS `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Content id',
  `add_time` datetime DEFAULT NULL COMMENT 'Add time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  `title` varchar(255) DEFAULT NULL COMMENT 'Content title',
  `description` varchar(255) DEFAULT NULL COMMENT 'Content description',
  `content` text DEFAULT NULL COMMENT 'Main content text',
  `images` text DEFAULT NULL COMMENT 'Content cover',
  `good_count` int(1) DEFAULT NULL COMMENT 'Like count',
  `collect_count` int(1) DEFAULT NULL COMMENT 'Collect count',
  `status` int(1) DEFAULT NULL COMMENT 'Status',
  `enabled` bit(1) DEFAULT NULL COMMENT 'Availability status',
  `user_id` int(11) DEFAULT NULL COMMENT 'User id',
  `category_id` int(11) DEFAULT NULL COMMENT 'Category id',
  `channels_id` int(11) DEFAULT NULL COMMENT 'Channles id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` (`id`, `add_time`, `update_time`, `title`, `description`, `content`, `images`, `good_count`, `collect_count`, `status`, `enabled`, `user_id`, `category_id`, `channels_id`) VALUES
	(1, '2021-08-25 23:44:23', '2021-08-25 23:44:28', 'New World Hits 1 Million Players On Day One, Server Capacity Increasing To Meet Demand', NULL, 'Amazon has provided an update on its newly launched PC MMO New World, revealing that more than 1 million people logged in on launch day and promising to increase the capacity of all servers to meet demand. In a statement on Twitter, Amazon said the demand for New World has been "staggering."', 'https://i.ibb.co/mzV92gW/newWorld.png', 0, 0, 1, b'1', 20, 1, 19),
	(2, '2021-08-26 15:08:43', '2021-08-26 15:08:43', 'Kena: Bridge of Spirits', NULL, 'After a couple of delays, Kena: Bridge of Spirits finally launches next week, September 21. It’s been a long time coming for the whimsical game, which was initially revealed alongside the PlayStation 5 and positioned to launch alongside it. We featured the title on our cover, but we’d forgive you if your memory has become hazy. Fear not, this handy primer should catch you up on the main beats if youre still on the fence about whether or not Kenas adventure is for you.', 'https://i.ibb.co/N3Jm4zP/kena-bridge-of-spirits-screenshot-04-en-29jul20.jpg,https://i.ibb.co/MhgBKS8/kena-bridge-of-spirits-screenshot-05.jpg', 0, 0, 1, b'1', 3, 4, 17),
	(3, '2021-08-27 15:11:01', '2021-08-27 15:11:01', 'Lightforged Ruinstrider - New Lightforged Draenei Paladin Class Mount in Patch 9.1.5', NULL, ' This talbuk mount has its armor is themed after the Lightforged Draenei Heritage Armor, and like all other Paladin class mounts, is the default  Divine Steed mount for Lightforged Draenei Paladins in 9.1.5.', 'https://i.ibb.co/N1fc8Yf/1033049.jpg', 0, 0, 1, b'1', 4, 1, 3),
	(4, '2021-09-02 15:16:58', '2021-09-02 15:16:58', 'Monster Hunter Stories 2: Wings Of Ruin', NULL, 'The mainline Monster Hunter series has seen a number of quality-of-life changes in recent years, but even with those changes the series is a tough sell for newcomers. On the other hand, the sequel to 2017’s Monster Hunter Stories might be the perfect on-ramp for those who want to start hunting. In Monster Hunter Stories 2: Wings of Ruin, Monstie riders are people who create bonds with monsters, entrusting them to fight alongside them in battle. The Stories series has a greater focus on narrative and monster collection, and it converts the epic monster confrontations into turn-based RPG battles, though players can still expect plenty of the franchise’s signature strategy and planning.', 'https://i.ibb.co/XxRzNmW/mh-stories-2-story-art.jpg', 0, 0, 1, b'1', 5, 4, 1),
	(5, '2021-09-10 11:31:20', '2021-09-10 11:31:20', 'How God of War Ragnaroks New Director Brings A Different Perspective To The Series', NULL, 'If youre familiar with the development of 2018s God of War, youll know how personal of a game it was for Cory Barlog. The games director has long talked about the importance of crafting Kratos return to the PlayStation scene as a father-son story that pushed the violent Greek god into a more caring figure. The gamble worked, and Sony Santa Monica was able to craft a thoughtful and epic tale that took the best of the original series and brought it to a new audience.', 'https://i.ibb.co/JqGRp7x/godofwarragnarok05.jpg', 4, 1, 1, b'1', 6, 2, 14),
	(6, '2021-09-11 13:49:23', '2021-09-11 13:49:28', 'How to train your Rot God', NULL, 'Kena: Bridge of Spirits fanart inspired by the cover of the first HttyD - movie.', 'https://i.ibb.co/nz0t0D9/FDH2t-TBXIAg-EEl.jpg', 0, 0, 1, b'1', 7, 1, 17),
	(7, '2021-09-12 09:44:23', '2021-09-12 23:44:28', 'New Monster Hunter Rise: Sunbreak Details Revealed At TGS 2021', NULL, 'Capcom’s Tokyo Game Show presentation this morning was all about Monster Hunter, showcasing new additions coming to existing games like Monster Hunter Stories 2 and Monster Hunter Rise. However, all eyes were on the significant expansion for Rise next summer, Monster Hunter Rise: Sunbreak.', 'https://i.ibb.co/mHTHdw0/mhsunbreak.jpg', 0, 1, 1, b'1', 8, 2, 18),
	(8, '2021-09-13 14:23:23', '2021-09-13 14:23:28', 'PS Plus Free Games For October 2021 Revealed, Featuring Hell Let Loose', NULL, 'Every month, PlayStation Plus subscribers reap the benefit of a handful of free games to go along with the other perks of the service. Since the launch of the PS5, one title has been dedicated to the new-generation console, and the trend continues moving into October.', 'https://i.ibb.co/jvcwr9c/playstation-plus-october.jpg', 0, 0, 1, b'1', 9, 1, 1),
	(9, '2021-09-14 23:44:23', '2021-09-14 23:44:28', 'Chris Pratt Voices Mario In Illuminations Movie, Full Cast Revealed', NULL, 'Shigeru Miyamoto crashed today’s Nintendo Direct to make some big announcements regarding the Mario animated movie from Illumination Entertainment. Not only did Miyamoto drop the planned release timeframe of the film, but an exact date for North America. He also revealed the star-studded cast of the movie.', 'https://i.ibb.co/FHk1xfx/mario-video-game-franchise-nintendo-bros-jumpman.jpg', 0, 1, 1, b'1', 10, 1, 1),
	(10, '2021-09-15 23:44:23', '2021-09-15 23:44:28', 'The Good Life Gets Xbox Demo Today, Comes To Game Pass At Launch', NULL, 'The Good Life, the bizarre photography mystery game by Deadly Premonition mastermind Hidetaka “Swery” Suehiro and his team at White Owls, has a demo on Xbox Game Pass available now. The crowdfunded game recently got a release date of October 15 after several years of development and multiple delays. The demo is good news for those wary about the title after its bumpy development cycle, and Xbox fans can rest even easier knowing it’s launching to Game Pass on day one.', 'https://i.ibb.co/LpnYk5q/the-good-life-dog-gameplay.jpg', 0, 0, 1, b'1', 11, 1, 1),
	(11, '2021-09-16 23:44:23', '2021-09-16 23:44:28', 'Horizon Forbidden West Developer Is Hiring For MMO', NULL, 'Information about Horizon Forbidden West is relatively scarce – and for good reason. The game is still a ways off, but we’ve been fortunate enough to see extensive gameplay footage and learn about Aloy’s visual/narrative evolution throughout the two games. However, according to a recent job posting, developer Guerrilla Games might be looking to branch out into more multiplayer-focused experiences. Whether this has anything to do with the Horizon franchise or a new IP scheduled for release in the very distant future remains to be seen. In any case, the team is looking to hire MMO experts.', 'https://i.ibb.co/vJkxBQK/horizon5.jpg', 0, 0, 1, b'1', 12, 2, 1),
	(12, '2021-09-17 23:44:23', '2021-09-17 23:44:28', 'Back 4 Blood Open Beta Early Impressions', NULL, 'The Back 4 Blood open beta is now live (you can learn how to get in here), meaning fans all around the world are jumping into the cooperative zombie shooter from the studio that brought us the original Left 4 Dead. While many of the key elements from that formative PvE co-op zombie shooter are in place with this spiritual successor, developer Turtle Rock Studios is changing up the game in myriad ways. I was able to go hands-on with the open beta ahead of its wide release to see how the game is coming along.', 'https://i.ibb.co/XbcwH03/bruiser.jpg', 0, 0, 1, b'1', 13, 4, 1),
	(13, '2021-09-18 23:44:23', '2021-09-18 23:44:28', 'How I feel after watching Zelda Breath of the Wild 2', NULL, 'After watching the trailer already can not wait, Link directly fly down into the sky city, the former red skin pig and stone man conjoined, this time the stone of Sika is also no longer, Link directly deep right hand of God to make the iron ball backwards.', 'https://i.ibb.co/7QyRrKn/5a73bcc2f02fd62d30573b4d229862dc.jpg', 0, 1, 1, b'1', 14, 4, 24),
	(14, '2021-09-19 23:44:23', '2021-10-22 13:25:15', 'Classically Beautiful Fan Designed Class Tier Sets', NULL, 'Several fans have been coming up with their own ideas for tier sets, perhaps in the hope that Blizzard will take notice of the interest in them and bring them back. Today spotlight is on a gorgeous collection of tier sets created by Redditor Alshyna. Check these out these designs, each drawing from a classic concept for each class', 'https://i.ibb.co/LnXb58j/1037340.jpg', 0, 1, 1, b'1', 15, 1, 3),
	(15, '2021-09-20 23:44:23', '2021-09-20 23:44:28', 'New League of Legends champion Vex revealed: release date, abilities, lore', NULL, 'Riot Games has revealed Vex the Gloomist, League of Legends 157th champion, as well as her abilities, ultimate, new skins, and her Season 11 release date — which is fast approaching. Here’s everything you need to know about the new LoL character.', 'https://i.ibb.co/jfyDX0s/new-league-of-legends-champion-vex-revealed-gloomy-mage-abilities-release-date-trailer-leaks-1248x70.webp', 1, 0, 1, b'1', 16, 2, 2),
	(16, '2021-09-21 23:44:23', '2021-09-21 23:44:28', 'How I Rate Dead Cells', NULL, 'I managed to get to the final level on my second attempt at Dead Cells and thought I had a clear understanding of the game by then. It was not until 63 games later that I finally took out the games final boss, and that when I realised that the detail and depth of this Motion Twin platform action game was far greater than I had imagined. Dead Cells has astounding game design - the ever-changing and fascinating levels, the wealth of weapons and equipment, and the incredibly exciting action gameplay combine to create a compelling gaming experience. It really is so much fun, because even after more than ten hours of exploring the fortress, I kept discovering something completely new.', 'https://i.ibb.co/8P7qCNs/v2-4d07905261ccfe5b04d5d899d300be8e-1440w.jpg', 3, 1, 1, b'1', 17, 5, 1),
	(17, '2021-09-22 23:44:23', '2021-09-22 23:44:28', 'God of War Valkyrie guide', NULL, 'Before you begin a Valkyrie fight, do three things: Fill your Runic attack meters. Begin every Valkyrie fight with back-to-back Runic attacks (L1 + R1 and L1 + R2). As Valkyries descend from their winged cocoons, they’re vulnerable. Attack then, and you’ll chip away a big portion of her health without resistance. (If your meters aren’t full, you can just stand around before the fight and wait for them to fill up.) Fill your Spartan Rage meters. Spartan Rage does two things. The obvious one is inflict massive amounts of damage. The less obvious one is restore your health as you attack. You can use it to finish off a Valkyrie or restore your low health — or both! (If your meters aren’t full, go find some enemies to kill. You can warp right back to the Mystic Gateway outside of the arena.) Bring a Resurrection Stone. If you die while you’re holding a Resurrection Stone, press Square and Atreus will bring him back to life. Any Resurrection Stone will do, and you can buy them at Brok or Sindri’s shop. Just remember that you can only hold one at a time.', 'https://i.ibb.co/yPwQXgv/God-of-War-Valkyrie-guide-0.webp', 1, 2, 1, b'1', 18, 3, 14),
	(18, '2021-09-23 23:44:23', '2021-09-23 23:44:28', 'Eastward Review – Pixel Paradise', NULL, 'Eastward feels like going back to the days of 80s anime on VHS, where you could experience a clandestine and unusual medium that you couldn’t find anywhere else. As John and Sam, players must head Eastward (whoa, that’s the title!) across a strange and surreal universe. While the characters and lore have a lot of heart and carry this journey to the end with ease, the combat, puzzles, and boss encounters leave a bit to be desired. An excellent score provides an incredible backdrop to pixel-perfect art, creating a whimsical and enchanting atmosphere for this quirky RPG that openly pays homage to titles like Earthbound. In fact, inside Eastward, there’s an entire game called Earth Born, complete with its own (in-game currency) gacha system that’s a blast to play when you just want to take a break from advancing the main story.', 'https://i.ibb.co/G9mqm2z/11111.jpg', 1, 1, 1, b'1', 19, 4, 1),
	(19, '2021-09-24 23:44:23', '2021-09-24 23:44:28', 'League of Legends’ next skin line, titled Cafe Cutie, is one of League’s most eye-catching skin lines to date. ', NULL, '11.23 | Cafe Cutie Annie, Soraka, Sivir and Gwen Splash Arts.', 'https://i.ibb.co/FsQNSQn/FDMo-g3-XIAMis-RF.jpg, https://i.ibb.co/f8B5w8m/FDMx-Nj-Xo-AQeb-EI.jpg, https://i.ibb.co/xMTP2wP/FDMy-B9-WWUAwycof.jpg', 0, 1, 1, b'1', 9, 2, 2),
	(20, '2021-09-25 23:44:23', '2021-09-25 23:44:28', '19 yrs ago today, “Grand Theft Auto: Vice City” was released. ', NULL, 'With an amazing soundtrack and fun storyline set in the 80’s, this game sold over 500,000 copies in 24 hrs of releasing and by 2008 sell would over 17.5 million units worldwide Did you own this GTA growing up?', 'https://i.ibb.co/0MsCqcC/FC33icd-UUAQrc-Zt.jpg, https://i.ibb.co/v3X5zXH/FC33icd-UUAc-XB24.jpg', 0, 0, 1, b'1', 23, 5, 4),
	(21, '2021-09-27 23:44:23', '2021-09-27 23:44:28', 'minecraft and me', NULL, 'one of my friend reach 1k so I made a render for him', 'https://i.ibb.co/J2tt6Hw/FDMrs-9-VQAIYMP9.jpg', 0, 0, 1, b'1', 5, 1, 5),
	(22, '2021-09-29 23:44:23', '2021-09-29 23:44:28', 'Ace Combat 7', NULL, 'ADF-11 Model Swap mod was released', 'https://i.ibb.co/MBcVMX4/FDHqy4l-XEAoqqu-P.jpg, https://i.ibb.co/QJ2b6vy/FDHqy4o-Xs-A8-ZGcx.jpg', 0, 0, 1, b'1', 17, 2, 6),
	(23, '2021-10-01 23:44:23', '2021-10-01 23:44:28', 'Cyberpunk', NULL, 'The Look', 'https://i.ibb.co/PMyyzmc/FCn-EM1b-UYAMt68r.jpg', 0, 0, 1, b'1', 4, 1, 7),
	(24, '2021-10-03 23:44:23', '2021-10-03 23:44:28', 'GTA5', NULL, 'Well......', 'https://i.ibb.co/VBdC26X/FCd-ZICBXo-AE5-BAo.jpg', 0, 0, 1, b'1', 22, 1, 4),
	(25, '2021-10-05 23:44:23', '2021-10-05 23:44:28', 'Red Dead Redemption 2 is still so beautiful', NULL, 'Red Dead Redemption 2 is still so beautiful', 'https://i.ibb.co/zQhS2qj/erpm0tns8fx71.png', 0, 0, 1, b'1', 6, 1, 22),
	(26, '2021-10-08 23:44:23', '2021-10-08 23:44:28', 'Duckcraft', NULL, 'Shuba Shuba!', 'https://i.ibb.co/3ynKzgh/FDFc-DGi-X0-AAy-T8-Z.jpg', 0, 0, 1, b'1', 20, 1, 5),
	(27, '2021-10-11 23:44:23', '2021-10-11 23:44:28', 'Diablo 3 sesson', NULL, 'Season 25 introduces new demonic powers', 'https://i.ibb.co/88m5rQ2/FDLru-N8-WYAEZu3o.jpg', 0, 0, 1, b'1', 19, 2, 8),
	(28, '2021-10-12 23:44:23', '2021-10-12 23:44:28', 'Ace Combat', NULL, 'When Ace Combat 8 will be released?', 'https://i.ibb.co/3mB8LWG/wz28oakhxax71.jpg', 0, 0, 1, b'1', 9, 1, 6),
	(29, '2021-10-13 23:44:23', '2021-10-13 23:44:28', 'Three years ago, Red Dead Redemption 2 released.', NULL, 'it brought one of my favorite video game characters of all time.', 'https://i.ibb.co/bRgP5bT/FCr-A0kr-Xo-Ag-Yr-Yd.jpg, https://i.ibb.co/MsDGZy1/FCr-A1l-EXMAg2m-N.jpg', 0, 0, 1, b'1', 10, 4, 22),
	(30, '2021-10-14 23:44:23', '2021-10-14 23:44:28', 'cyber7720', NULL, 'The Streets.', 'https://i.ibb.co/ZNMcyrr/FC4-jb-WXs-Ac8-tq.jpg, https://i.ibb.co/xG7fKQG/FC4-j9l-XIAE-s0-P.jpg', 0, 0, 1, b'1', 11, 1, 7),
	(31, '2021-10-15 23:44:23', '2021-10-15 23:44:28', 'BRO TI IS OVER A WEEK AGO AND DOTA 2 IS TRENDING?', NULL, 'I guess this new DOTA2 hero is good.', 'https://i.ibb.co/qRsM1Lx/FDJvt99-X0-AQ2ld-A.jpg', 0, 0, 1, b'1', 12, 1, 9),
	(32, '2021-10-16 23:44:23', '2021-10-16 23:44:28', 'Introducing Console WarriorZ! A new DBFZ Online Tournament Series for EU!', NULL, 'Due to popular demand I haveve put this together to help those who dont play on PC a chance to showcase their skills!', 'https://i.ibb.co/Lx9fRG4/EYyku-GCX0-AEJlew.jpg', 0, 0, 1, b'1', 15, 1, 10),
	(33, '2021-10-17 23:44:23', '2021-10-17 23:44:28', 'ff14', NULL, 'More degeneracy', 'https://i.ibb.co/b7Z8xNn/FDIZdj2-WUAQfff-J.jpg, https://i.ibb.co/wKZwwbG/FDIZdju-XIAcp-Afc.jpg, https://i.ibb.co/VNzFsd7/FDIZdk4-Xs-AIGD3m.jpg, https://i.ibb.co/XtRWT8P/FDIZdk-IX0-AYr-L4-I.jpg', 0, 0, 1, b'1', 14, 1, 11),
	(34, '2021-10-18 23:44:23', '2021-10-18 23:44:28', 'Diablo', NULL, 'Well. After consistently gambling for quivers for DAYS I have now got that primal that was bound to drop... Except it is the wrong one of course -_-', 'https://i.ibb.co/2YsNFYF/FC-G94-VWQAw-Qvw-S.jpg', 0, 0, 1, b'1', 11, 1, 8),
	(35, '2021-10-19 23:44:23', '2021-10-19 23:44:28', 'dota', NULL, 'Marci powers her way into Dota along with a new treasure and the 7.30e gameplay update --', 'https://i.ibb.co/PN6GWQ2/FC0o-Rib-VEAI06-So.png', 0, 0, 1, b'1', 23, 2, 9),
	(36, '2021-10-20 23:44:23', '2021-10-20 23:44:28', 'I mean... this is the best alt after all', NULL, 'android 21 from dragon ball fighter z is black!', 'https://i.ibb.co/N6n8yk4/FDHa-ab-WEAQHw-GP.jpg', 0, 0, 1, b'1', 21, 1, 10),
	(37, '2021-10-21 23:44:23', '2021-10-21 23:44:28', 'dungeon of ff14', NULL, 'Within The Grand Cosmos dungeon. More colors, more flowers and more pretty things!', 'https://i.ibb.co/cr51pmF/E95-Pa7-XWEAc-Cq1n.jpg, https://i.ibb.co/d6j94JH/E95-Pf-Rf-XEAIt-Ejl.jpg, https://i.ibb.co/b5F8FJM/E95-PRl-ZXs-AEh-EOd.jpg', 0, 0, 1, b'1', 22, 1, 11),
	(38, '2021-10-22 23:44:23', '2021-10-22 23:44:28', 'So Danny Trejo is in Far Cry 6!', NULL, 'LOL. Danny Trejo. GOTY!', 'https://i.ibb.co/1X41TC4/FDLcono-WQAoi-JIP.jpg', 0, 0, 1, b'1', 8, 1, 13),
	(39, '2021-10-23 23:44:23', '2021-10-23 23:44:28', 'Here are other FromSoftware statues from different events', NULL, '"Warrior Knight" that was made for a giveaway and is also at FromSoftware offices; Cleric Beast at Tokyo Games Show 2015;"Red Knight" fountain at E3 2015; Corrupted Monk at Gamescom 2018;', 'https://i.ibb.co/4Fc2bht/FC28hjw-XEAI2b2-Z.jpg, https://i.ibb.co/f1qwBc7/FC28-W4-QXEAAt4pr.jpg', 0, 0, 1, b'1', 15, 1, 15),
	(40, '2021-10-24 23:44:23', '2021-10-24 23:44:28', 'hearthstone tavern brawl', NULL, '“Opponent’s” should have an apostrophe. I’ve noticed so many grammatical errors lately. Also, what even is this tavern brawl?', 'https://i.ibb.co/pjyrKDN/ig25mbwonex71.jpg', 0, 0, 1, b'1', 16, 1, 16),
	(41, '2021-10-25 23:44:23', '2021-10-25 23:44:28', 'Monster Hunter', NULL, 'Monster Hunter Rise SoulBreak New Monsters: Malzeno & Shogun Ceanataur', 'https://i.ibb.co/gzcRtLM/y6b2a1yu7nq71.jpg', 0, 0, 1, b'1', 21, 2, 18),
	(42, '2021-10-26 23:44:23', '2021-10-26 23:44:28', 'FarCry6', NULL, 'Pretty Nice View of Yara tbf.', 'https://i.ibb.co/Lgz0Fgs/FDL98e-CWUAUskz-L.jpg', 0, 0, 1, b'1', 19, 1, 13),
	(43, '2021-10-27 23:44:23', '2021-10-27 23:44:28', 'For Sekiro only. xD', NULL, 'Finally the platinum trophy after 2.5 years and 5 NG cycles! Wohooo! Now all my Souls games have platinum, and can’t wait for Elden Ring!', 'https://i.ibb.co/g3RMhZy/y2mshuoy5nw71.jpg', 0, 0, 1, b'1', 13, 1, 15),
	(44, '2021-10-28 23:44:23', '2021-10-28 23:44:28', 'Amazon Games New World Has Lost Almost 70% Of Its Players', NULL, 'I’m one of them. And it’s because I cannot connect to the game. The game worked fine up until a few updates ago and now connecting to the servers requires about 10 restarts of the game.', 'https://i.ibb.co/vPr1htY/New-World-Decline-jpg.webp', 0, 0, 1, b'1', 12, 2, 19),
	(45, '2021-10-29 23:44:23', '2021-10-29 23:44:28', 'NieR', NULL, 'I just got 100% and the platinum, it is so hard to say goodbye. I have not done this for any other game, definetly my favorite videogame ever next to Lisa the Painful. Playing my last Angel of death run through the map before I delete my data.', 'https://i.ibb.co/HhqYM8L/pjepp9p6jbu71.jpg', 1, 1, 1, b'1', 19, 4, 20),
	(46, '2021-10-30 23:44:23', '2021-10-30 23:44:28', 'New Heroes Interface', NULL, 'This is making me hopeful for a portrait randomizer coming soon, as this is the perfect way to do it.', 'https://i.ibb.co/yNQVgzS/96uawo9j58x71.png', 0, 0, 1, b'1', 5, 1, 16),
	(47, '2021-11-01 23:44:23', '2021-11-01 23:44:28', 'my 2B', NULL, 'Finally got the game and it is been super fun!', 'https://i.ibb.co/4JmY9hp/wcqr9bbv8nq71.jpg', 0, 0, 1, b'1', 19, 1, 20),
	(48, '2021-11-02 23:44:23', '2021-11-02 23:44:28', 'overwatch', NULL, 'whats this skin thats popped up? how do u get it?', 'https://i.ibb.co/74Sy1Cy/tq3f49fnffx71.jpg', 0, 0, 1, b'1', 9, 1, 21),
	(49, '2021-11-03 23:44:23', '2021-11-03 23:44:28', 'It is been quite some time since Resident Evil Village came out. ', NULL, 'So, which one of the 4 lords was your favorite?', 'https://i.ibb.co/S7MrjVY/FDILe93-WUAg-Toel.jpg, https://i.ibb.co/cT9bf42/FDILf-q-Xo-AAljib.jpg, https://i.ibb.co/WGYZdrB/FDILfi6-Xs-AITz3u.jpg, https://i.ibb.co/vVnHLyS/FDILg-Xa-WUAMl-Fr-N.jpg', 1, 0, 1, b'1', 23, 1, 23),
	(50, '2021-11-04 23:44:23', '2021-11-04 23:44:28', 'Zelda', NULL, 'So it is canon that Zelda doesnot know how to cook and Link is like she trying her best ya', 'https://i.ibb.co/Vqn1Cjp/FC4s-E3-NVc-AUFfxo.jpg, https://i.ibb.co/mb5WPhb/FC4s-FBFVEAIDD8-S.jpg', 0, 0, 1, b'1', 18, 1, 24),
	(51, '2021-11-04 23:44:23', '2021-11-04 23:44:28', 'thewitcher3', NULL, 'what a pose to die in', 'https://i.ibb.co/wCmNSY1/FC-n-Nq-YUUAA5-Dh6.jpg', 0, 0, 1, b'1', 6, 1, 25),
	(52, '2021-11-04 23:44:23', '2021-11-04 23:44:28', 'academy skin', NULL, 'overwatch - dva: academy skin ver. (nendoroid)', 'https://i.ibb.co/SQRZtx7/E8-JPwhz-XIAw-T2-Ww.jpg, https://i.ibb.co/gD2tYqF/E8-JPw-W2-XEAEwh-BT.jpg', 1, 1, 1, b'1', 14, 1, 21),
	(53, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'Resident Evil Village', NULL, 'Castle Dimitrescu from Resident Evil Village.  It was my favorite area of the game, I could’ve played in this location forever', 'https://i.ibb.co/JtkGd02/FCo9-OYh-Xs-AEbg2w.jpg, https://i.ibb.co/pdXgSKY/FCo9-OYi-WEAo-RPMP.jpg', 2, 0, 1, b'1', 20, 1, 23),
	(54, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'Hi there fellow witchers!', NULL, 'I’m about to clear this Hanse base for the last time, finally leveling Geralt up to my goal of lvl 60 before starting NG+. Will update after taking a small break!', 'https://i.ibb.co/LzPkjrX/f2eqk8tfw1x71.jpg', 2, 1, 1, b'1', 7, 1, 25),
	(55, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'Left Behind is some great DLC', NULL, ' The first game had me pulling my hair for grounded+ ', 'https://i.ibb.co/DrSCTqD/xga8t0e278x71.jpg', 0, 1, 1, b'1', 7, 5, 26),
	(56, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'Civilization build', NULL, 'I have just built the Stonehenge in Civilization VI', 'https://i.ibb.co/C6LcCBN/FDJ6-Rr9-WEAED8l-O.png', 1, -1, 1, b'1', 15, 1, 27),
	(57, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'Rise of the Tomb Raider is a polished game', NULL, 'Happy Rise of the Tomb Raider month.', 'https://i.ibb.co/4NHP8QP/FDIt-At-SXs-Ac-Yefq.jpg, https://i.ibb.co/hgN1R4L/FDIt-Bp-AWUAM30vz.jpg', 0, 0, 1, b'1', 1, 1, 28),
	(58, '2021-11-05 23:44:23', '2021-11-05 23:44:28', '(PCDD)', NULL, 'Rise of the Tomb Raider is Free for Amazon Prime Members via Prime Gaming.', 'https://i.ibb.co/PNgh9J6/FDHs-Urt-Xs-Actw-Go.jpg', 0, 0, 1, b'1', 9, 1, 28),
	(59, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'last of us', NULL, 'What Are The Possibilities Of Joel & Ellie Getting Into Crossover Games?', 'https://i.ibb.co/w4XjfFZ/opse6w5r9xw71.png', 0, 1, 1, b'1', 8, 1, 26),
	(60, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'Civilization', NULL, 'Got my Diety hat trick!', 'https://i.ibb.co/pJ4V48q/xpumcro6xsv41.jpg', 0, 1, 1, b'1', 17, 1, 27),
	(61, '2021-11-05 23:44:23', '2021-11-12 18:30:10', 'fifa22', NULL, 'My first week on fifa 22 Div 4 rank 3 got me this !!', 'https://i.ibb.co/nBxqYsD/FDWYQ4-NWYAI2u-BO.jpg', 2, 1, 1, b'1', 4, 1, 12),
	(62, '2021-11-05 23:44:23', '2021-11-05 23:44:28', 'Beast card', NULL, 'Happy days…', 'https://i.ibb.co/N9f2gf0/dtcbvlbzncx71.jpg', 0, 0, 1, b'1', 4, 1, 12);
	