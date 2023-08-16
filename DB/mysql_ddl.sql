CREATE TABLE `mail_config`
(
    `mail_code`     int NOT NULL,
    `mail_name`     varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `mail_address`  varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `mail_username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `mail_password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `environment`   varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `mail_host`     varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `mail_port`     varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `mail_protocol` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `mail_auth`     varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `valid`         char(1) COLLATE utf8mb4_general_ci      DEFAULT NULL,
    PRIMARY KEY (`mail_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `mail_list`
(
    `id`              int NOT NULL,
    `mail_address`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `receiver_type`   varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `receiver_type2`  varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    ` receiver_type3` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `mail_model`
(
    `id`            int NOT NULL,
    `model_type`    int                                     DEFAULT NULL,
    `model_name`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `ch_subject`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `en_subject`    varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `ch`            varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `en`            varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `modified_date` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `decimal_test`
(
    `id`  int NOT NULL,
    `age` decimal(6, 3) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


CREATE TABLE `device`
(
    `device_id`    int NOT NULL,
    `device_name`  varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `point_number` varchar(32) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`device_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `operation_log`
(
    `id`           int NOT NULL,
    `log_info`     varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作日志内容',
    `created_by`   varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人id',
    `created_time` datetime                                DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;