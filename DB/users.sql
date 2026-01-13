/*
 Navicat Premium Data Transfer

 Source Server         : NTFurbo _ 192.168.30.140
 Source Server Type    : MySQL
 Source Server Version : 80401
 Source Host           : 192.168.30.140:3306
 Source Schema         : nfturbo

 Target Server Type    : MySQL
 Target Server Version : 80401
 File Encoding         : 65001

 Date: 11/01/2026 07:38:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID（自增主键）',
  `gmt_create` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '最后更新时间',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码哈希',
  `state` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户状态（ACTIVE，FROZEN）',
  `invite_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请码',
  `telephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `inviter_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请人用户ID',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `profile_photo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  `block_chain_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区块链地址',
  `block_chain_platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区块链平台',
  `certification` tinyint(1) NULL DEFAULT NULL COMMENT '实名认证状态（TRUE或FALSE）',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `id_card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证no',
  `user_role` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户角色',
  `deleted` int(0) NULL DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
  `lock_version` int(0) NULL DEFAULT NULL COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (29, '2024-05-26 12:07:38', '2024-06-10 14:14:20', '藏家_zH9sQA0bob1', 'e7beea81b7a03b38508428fbeeb3c69a', 'ACTIVE', NULL, '18000000000', NULL, NULL, 'https://nfturbo-file.oss-cn-hangzhou.aliyuncs.com/profile/29/O1CN014qjUuW1IKL1Ur3fGI_!!2213143710874.jpg_Q75.jpg_.avif', 'iaa1w57n8adlxcvgas93c5x4j36msen3uxl0jxg7mg', 'WEN_CHANG', 1, '446ad47811888a04c6610741aff349c1', '670c02c9ce418d783fad1622c007ace8ac5f47acb1a393455f794d541f80d58c', 'CUSTOMER', 0, 10);
INSERT INTO `users` VALUES (36, '2024-07-06 14:13:26', '2024-07-06 14:13:26', '藏家_R3qWhY3333', '34347c343003e57232a5d21f14fe399e', 'ACTIVE', NULL, '13333333333', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ADMIN', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
