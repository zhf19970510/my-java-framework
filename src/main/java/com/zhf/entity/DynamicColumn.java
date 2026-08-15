package com.zhf.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * t_dynamic_column 表实体类
 * <p>
 * 该表覆盖了MySQL生产环境中常见的全部数据类型，包括：
 * 整数类型(TINYINT/SMALLINT/MEDIUMINT/INT/BIGINT/UNSIGNED INT)、
 * 定点与浮点类型(DECIMAL/FLOAT/DOUBLE)、位类型(BIT)、
 * 字符串类型(CHAR/VARCHAR/TINYTEXT/TEXT/MEDIUMTEXT/LONGTEXT)、
 * 二进制类型(BINARY/VARBINARY/BLOB)、
 * 日期时间类型(DATE/TIME/DATETIME/TIMESTAMP/YEAR)、
 * 枚举类型(ENUM)、集合类型(SET)、JSON类型。
 * <p>
 * 主键 pk_id 使用 UUID 字符串，由业务代码主动塞值，非自增主键。
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
@Data
public class DynamicColumn {

    // ==================== 主键 ====================
    /** 主键ID(UUID,业务代码主动塞值,非自增) */
    private String pkId;

    // ==================== 整数类型 ====================
    /** TINYINT[-128~127] */
    private Integer colTinyint;
    /** SMALLINT[-32768~32767] */
    private Integer colSmallint;
    /** MEDIUMINT[-8388608~8388607] */
    private Integer colMediumint;
    /** INT标准整数类型 */
    private Integer colInt;
    /** BIGINT大整数类型 */
    private Long colBigint;
    /** INT UNSIGNED无符号整数[0~4294967295] */
    private Long colUnsignedInt;

    // ==================== 定点与浮点类型 ====================
    /** DECIMAL高精度定点数(适用于金额) */
    private BigDecimal colDecimal;
    /** FLOAT单精度浮点数 */
    private Float colFloat;
    /** DOUBLE双精度浮点数 */
    private Double colDouble;

    // ==================== 位类型 ====================
    /** BIT位字段类型(8位) */
    private byte[] colBit;

    // ==================== 字符串类型 ====================
    /** CHAR定长字符串(最大10字符) */
    private String colChar;
    /** VARCHAR变长字符串(最大255字符) */
    private String colVarchar;
    /** TINYTEXT短文本(最大255字节) */
    private String colTinytext;
    /** TEXT普通文本(最大65535字节) */
    private String colText;
    /** MEDIUMTEXT中等文本(最大16MB) */
    private String colMediumtext;
    /** LONGTEXT长文本(最大4GB) */
    private String colLongtext;

    // ==================== 二进制类型 ====================
    /** BINARY定长二进制(16字节) */
    private byte[] colBinary;
    /** VARBINARY变长二进制(最大255字节) */
    private byte[] colVarbinary;
    /** BLOB二进制大对象(最大65535字节) */
    private byte[] colBlob;

    // ==================== 日期时间类型 ====================
    /** DATE日期类型(YYYY-MM-DD) */
    private LocalDate colDate;
    /** TIME时间类型(HH:MM:SS) */
    private LocalTime colTime;
    /** DATETIME日期时间类型 */
    private LocalDateTime colDatetime;
    /** TIMESTAMP时间戳类型(范围1970~2038) */
    private LocalDateTime colTimestamp;
    /** YEAR年份类型(4位数字) */
    private Integer colYear;

    // ==================== 枚举与集合类型 ====================
    /** ENUM枚举类型(ENABLED/DISABLED/PENDING) */
    private String colEnum;
    /** SET集合类型(READ/WRITE/DELETE/ADMIN) */
    private String colSet;

    // ==================== JSON类型 ====================
    /** JSON类型(存储JSON格式数据) */
    private String colJson;

    // ==================== 审计字段 ====================
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新人 */
    private String updateBy;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
