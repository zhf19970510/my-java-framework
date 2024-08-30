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

 Date: 30/08/2024 22:57:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept`  (
  `dept_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `dept_level` int NULL DEFAULT NULL COMMENT '部门层级',
  `sup_dept` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '直接上级机构id',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dept
-- ----------------------------
INSERT INTO `t_dept` VALUES ('1', '总公司', 1, NULL);
INSERT INTO `t_dept` VALUES ('1-1', '北京分公司', 2, '1');
INSERT INTO `t_dept` VALUES ('1-1-1', '北京分公司开发部', 3, '1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1', '北京开发部一组', 4, '1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1-1', '北京开发部一组前端', 5, '1-1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1-1-1', '北京开发部一组前端1小组', 6, '1-1-1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1-1-1-1', '北京开发部一组前端1小组项目1组', 7, '1-1-1-1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1-1-2', '北京开发部一组前端2小组', 6, '1-1-1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1-1-3', '北京开发部一组前端3小组', 6, '1-1-1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1-2', '北京开发部一组后端', 5, '1-1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-1-3', '北京开发部一组大数据', 5, '1-1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-2', '北京开发部二组', 4, '1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-1-3', '北京开发部三组', 4, '1-1-1');
INSERT INTO `t_dept` VALUES ('1-1-2', '北京分公司产品部', 3, '1-1');
INSERT INTO `t_dept` VALUES ('1-1-2-1', '北京产品部一组', 4, '1-1-2');
INSERT INTO `t_dept` VALUES ('1-1-2-1-1', '北京产品部一组项目1组', 5, '1-1-2-1');
INSERT INTO `t_dept` VALUES ('1-1-2-1-2', '北京产品部一组项目2组', 5, '1-1-2-1');
INSERT INTO `t_dept` VALUES ('1-1-2-1-3', '北京产品部一组项目3组', 5, '1-1-2-1');
INSERT INTO `t_dept` VALUES ('1-1-2-2', '北京产品部二组', 4, '1-1-2');
INSERT INTO `t_dept` VALUES ('1-1-2-2-1', '北京产品部二组项目1组', 5, '1-1-2-2');
INSERT INTO `t_dept` VALUES ('1-1-2-2-2', '北京产品部二组项目2组', 5, '1-1-2-2');
INSERT INTO `t_dept` VALUES ('1-1-2-2-3', '北京产品部二组项目3组', 5, '1-1-2-2');
INSERT INTO `t_dept` VALUES ('1-1-2-3', '北京产品部三组', 4, '1-1-2');
INSERT INTO `t_dept` VALUES ('1-1-3', '北京分公司运营部', 3, '1-1');
INSERT INTO `t_dept` VALUES ('1-1-3-1', '北京运营部一组', 4, '1-1-3');
INSERT INTO `t_dept` VALUES ('1-1-3-2', '北京运营部二组', 4, '1-1-3');
INSERT INTO `t_dept` VALUES ('1-1-3-3', '北京运营部三组', 4, '1-1-3');
INSERT INTO `t_dept` VALUES ('1-1-4', '北京分公司安全部', 3, '1-1');
INSERT INTO `t_dept` VALUES ('1-1-4-1', '北京安全部一组', 4, '1-1-4');
INSERT INTO `t_dept` VALUES ('1-1-4-2', '北京安全部二组', 4, '1-1-4');
INSERT INTO `t_dept` VALUES ('1-1-4-3', '北京安全部三组', 4, '1-1-4');
INSERT INTO `t_dept` VALUES ('1-1-5', '北京分公司后勤部', 3, '1-1');
INSERT INTO `t_dept` VALUES ('1-1-5-1', '北京后勤部一组', 4, '1-1-5');
INSERT INTO `t_dept` VALUES ('1-1-5-2', '北京后勤部二组', 4, '1-1-5');
INSERT INTO `t_dept` VALUES ('1-1-5-3', '北京后勤部三组', 4, '1-1-5');
INSERT INTO `t_dept` VALUES ('1-2', '上海分公司', 2, '1');
INSERT INTO `t_dept` VALUES ('1-3', '深圳分公司', 2, '1');

SET FOREIGN_KEY_CHECKS = 1;
