/*
 Navicat Premium Data Transfer

 Source Server         : localhost_mysql
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : test01

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 26/12/2024 22:19:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_main_group
-- ----------------------------
DROP TABLE IF EXISTS `t_main_group`;
CREATE TABLE `t_main_group`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_date` datetime NULL DEFAULT NULL,
  `crt_date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_main_group
-- ----------------------------
INSERT INTO `t_main_group` VALUES ('1', '1', '1', '1', '2024-12-26 21:45:03', '2024-12-26 21:45:09');
INSERT INTO `t_main_group` VALUES ('2', '2', '2', '1', '2024-12-26 21:45:21', '2024-12-26 21:45:24');
INSERT INTO `t_main_group` VALUES ('3', '3', '3', '1', '2024-12-26 21:46:06', '2024-12-26 21:46:10');
INSERT INTO `t_main_group` VALUES ('4', '4', '4', '3', '2024-12-26 21:46:27', '2024-12-26 21:46:37');
INSERT INTO `t_main_group` VALUES ('5', '5', '5', '2', '2024-12-26 21:46:50', '2024-12-26 21:46:54');
INSERT INTO `t_main_group` VALUES ('6', '6', '6', '2', '2024-12-26 21:47:06', '2024-12-26 21:47:13');
INSERT INTO `t_main_group` VALUES ('7', '7', '7', '2', '2024-12-26 21:47:31', '2024-12-26 21:47:36');

SET FOREIGN_KEY_CHECKS = 1;
