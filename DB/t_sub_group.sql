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

 Date: 26/12/2024 22:19:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_sub_group
-- ----------------------------
DROP TABLE IF EXISTS `t_sub_group`;
CREATE TABLE `t_sub_group`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `main_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `admin_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stru_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sub_group
-- ----------------------------
INSERT INTO `t_sub_group` VALUES ('1', '1', 'aa', '001');
INSERT INTO `t_sub_group` VALUES ('10', '2', 'aa', '008');
INSERT INTO `t_sub_group` VALUES ('11', '3', 'dd', '009');
INSERT INTO `t_sub_group` VALUES ('12', '3', 'ff', '010');
INSERT INTO `t_sub_group` VALUES ('13', '4', 'gg', '011');
INSERT INTO `t_sub_group` VALUES ('14', '4', 'gg', '012');
INSERT INTO `t_sub_group` VALUES ('15', '5', 'hh', '013');
INSERT INTO `t_sub_group` VALUES ('16', '5', 'hh', '014');
INSERT INTO `t_sub_group` VALUES ('17', '6', 'ii', '015');
INSERT INTO `t_sub_group` VALUES ('18', '6', 'jj', '016');
INSERT INTO `t_sub_group` VALUES ('19', '7', 'kk', '017');
INSERT INTO `t_sub_group` VALUES ('2', '1', 'aa', '002');
INSERT INTO `t_sub_group` VALUES ('20', '7', 'll', '018');
INSERT INTO `t_sub_group` VALUES ('21', '7', 'kk', '019');
INSERT INTO `t_sub_group` VALUES ('3', '1', 'aa', '003');
INSERT INTO `t_sub_group` VALUES ('4', '1', 'bb', '004');
INSERT INTO `t_sub_group` VALUES ('5', '1', 'cc', '005');
INSERT INTO `t_sub_group` VALUES ('6', '1', 'aa', '006');
INSERT INTO `t_sub_group` VALUES ('7', '2', 'aa', '003');
INSERT INTO `t_sub_group` VALUES ('8', '2', 'bb', '004');
INSERT INTO `t_sub_group` VALUES ('9', '2', 'cc', '007');

SET FOREIGN_KEY_CHECKS = 1;
