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

 Date: 30/08/2024 22:57:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dw_dept_rel
-- ----------------------------
DROP TABLE IF EXISTS `dw_dept_rel`;
CREATE TABLE `dw_dept_rel`  (
  `sup_dept` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上级机构id',
  `sub_dept` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下级机构id',
  PRIMARY KEY (`sup_dept`, `sub_dept`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dw_dept_rel
-- ----------------------------
INSERT INTO `dw_dept_rel` VALUES ('1', '1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-3-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-3-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-3-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-4');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-4-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-4-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-4-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-5');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-5-1');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-5-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-1-5-3');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-2');
INSERT INTO `dw_dept_rel` VALUES ('1', '1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-3-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-3-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-3-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-4');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-4-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-4-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-4-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-5');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-5-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-5-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1', '1-1-5-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1', '1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1', '1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1', '1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1', '1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1', '1-1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1', '1-1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1', '1-1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1-1', '1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1-1', '1-1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1-1-1', '1-1-1-1-1-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1-2', '1-1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-1-3', '1-1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-2', '1-1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-1-3', '1-1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-2', '1-1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-1-3', '1-1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2', '1-1-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-1', '1-1-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-1', '1-1-2-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-1', '1-1-2-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-1', '1-1-2-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-1-1', '1-1-2-1-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-1-2', '1-1-2-1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-1-3', '1-1-2-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-2', '1-1-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-2', '1-1-2-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-2', '1-1-2-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-2', '1-1-2-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-2-1', '1-1-2-2-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-2-2', '1-1-2-2-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-2-3', '1-1-2-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-2-3', '1-1-2-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-3', '1-1-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-3', '1-1-3-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-3', '1-1-3-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-3', '1-1-3-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-3-1', '1-1-3-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-3-2', '1-1-3-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-3-3', '1-1-3-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-4', '1-1-4');
INSERT INTO `dw_dept_rel` VALUES ('1-1-4', '1-1-4-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-4', '1-1-4-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-4', '1-1-4-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-4-1', '1-1-4-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-4-2', '1-1-4-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-4-3', '1-1-4-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-5', '1-1-5');
INSERT INTO `dw_dept_rel` VALUES ('1-1-5', '1-1-5-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-5', '1-1-5-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-5', '1-1-5-3');
INSERT INTO `dw_dept_rel` VALUES ('1-1-5-1', '1-1-5-1');
INSERT INTO `dw_dept_rel` VALUES ('1-1-5-2', '1-1-5-2');
INSERT INTO `dw_dept_rel` VALUES ('1-1-5-3', '1-1-5-3');
INSERT INTO `dw_dept_rel` VALUES ('1-2', '1-2');
INSERT INTO `dw_dept_rel` VALUES ('1-3', '1-3');

SET FOREIGN_KEY_CHECKS = 1;
