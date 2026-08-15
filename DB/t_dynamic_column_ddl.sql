-- =====================================================================
-- 表名: t_dynamic_column
-- 注释: mybatis批量插入改写表
-- 说明: 该表覆盖了MySQL生产环境中常见的全部数据类型，
--       主键使用UUID字符串(业务代码主动塞值，非自增主键)，
--       用于测试for循环单值插入、SqlSession BATCH模式批量插入、
--       以及SQL改写(insert into...values(...)(...)...)批量插入的性能差异。
-- =====================================================================

CREATE TABLE IF NOT EXISTS `t_dynamic_column` (
    -- ==================== 主键(非自增，业务代码塞UUID) ====================
    `pk_id`              VARCHAR(64)    NOT NULL                        COMMENT '主键ID(UUID,业务代码主动塞值,非自增)',

    -- ==================== 整数类型 ====================
    `col_tinyint`        TINYINT        DEFAULT NULL                    COMMENT 'TINYINT[-128~127]',
    `col_smallint`       SMALLINT       DEFAULT NULL                    COMMENT 'SMALLINT[-32768~32767]',
    `col_mediumint`      MEDIUMINT      DEFAULT NULL                    COMMENT 'MEDIUMINT[-8388608~8388607]',
    `col_int`            INT            DEFAULT NULL                    COMMENT 'INT标准整数类型',
    `col_bigint`         BIGINT         DEFAULT NULL                    COMMENT 'BIGINT大整数类型',
    `col_unsigned_int`  INT UNSIGNED   DEFAULT NULL                    COMMENT 'INT UNSIGNED无符号整数[0~4294967295]',

    -- ==================== 定点与浮点类型 ====================
    `col_decimal`        DECIMAL(20,4)  DEFAULT NULL                    COMMENT 'DECIMAL高精度定点数(适用于金额)',
    `col_float`          FLOAT          DEFAULT NULL                    COMMENT 'FLOAT单精度浮点数',
    `col_double`         DOUBLE         DEFAULT NULL                    COMMENT 'DOUBLE双精度浮点数',

    -- ==================== 位类型 ====================
    `col_bit`            BIT(8)         DEFAULT NULL                    COMMENT 'BIT位字段类型(8位)',

    -- ==================== 字符串类型 ====================
    `col_char`           CHAR(10)       DEFAULT NULL                    COMMENT 'CHAR定长字符串(最大10字符)',
    `col_varchar`        VARCHAR(255)   DEFAULT NULL                    COMMENT 'VARCHAR变长字符串(最大255字符)',
    `col_tinytext`       TINYTEXT                                       COMMENT 'TINYTEXT短文本(最大255字节)',
    `col_text`           TEXT                                           COMMENT 'TEXT普通文本(最大65535字节)',
    `col_mediumtext`     MEDIUMTEXT                                     COMMENT 'MEDIUMTEXT中等文本(最大16MB)',
    `col_longtext`       LONGTEXT                                       COMMENT 'LONGTEXT长文本(最大4GB)',

    -- ==================== 二进制类型 ====================
    `col_binary`         BINARY(16)     DEFAULT NULL                    COMMENT 'BINARY定长二进制(16字节)',
    `col_varbinary`      VARBINARY(255) DEFAULT NULL                    COMMENT 'VARBINARY变长二进制(最大255字节)',
    `col_blob`           BLOB                                           COMMENT 'BLOB二进制大对象(最大65535字节)',

    -- ==================== 日期时间类型 ====================
    `col_date`           DATE           DEFAULT NULL                    COMMENT 'DATE日期类型(YYYY-MM-DD)',
    `col_time`           TIME           DEFAULT NULL                    COMMENT 'TIME时间类型(HH:MM:SS)',
    `col_datetime`       DATETIME       DEFAULT NULL                    COMMENT 'DATETIME日期时间类型',
    `col_timestamp`      TIMESTAMP      NULL DEFAULT NULL               COMMENT 'TIMESTAMP时间戳类型(范围1970~2038)',
    `col_year`           YEAR           DEFAULT NULL                    COMMENT 'YEAR年份类型(4位数字)',

    -- ==================== 枚举与集合类型 ====================
    `col_enum`           ENUM('ENABLED','DISABLED','PENDING') DEFAULT 'ENABLED' COMMENT 'ENUM枚举类型',
    `col_set`            SET('READ','WRITE','DELETE','ADMIN')  DEFAULT NULL      COMMENT 'SET集合类型(可多选)',

    -- ==================== JSON类型(MySQL 5.7+) ====================
    `col_json`           JSON           DEFAULT NULL                    COMMENT 'JSON类型(存储JSON格式数据)',

    -- ==================== 审计字段 ====================
    `create_by`          VARCHAR(64)    DEFAULT NULL                    COMMENT '创建人',
    `create_time`        DATETIME       DEFAULT NULL                    COMMENT '创建时间',
    `update_by`          VARCHAR(64)    DEFAULT NULL                    COMMENT '更新人',
    `update_time`        DATETIME       DEFAULT NULL                    COMMENT '更新时间',

    PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='mybatis批量插入改写表';
