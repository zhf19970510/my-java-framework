package com.zhf.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.zhf.entity.dto.SnowIdDTO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SnowUtil {

    public static final int DATA_SIZE = 32;

    private static SnowIdDTO snowIdDTO = new SnowIdDTO();

    /**
     * 默认的雪花算法生成Id（workerId = 0，datacenterId = 0）
     *
     * @return snowId
     */
    public static String getSnowId() {
        return getSnowId(snowIdDTO);
    }

    public static String getSnowId(SnowIdDTO snowIdDTO) {
        return getSnowId(snowIdDTO.getWorkerId(), snowIdDTO.getDataCenterId());
    }

    public static String getSnowId(long workerId, long datacenterId) {
        return Long.toString(IdUtil.createSnowflake(workerId, datacenterId).nextId());
    }

    /**
     * 默认的雪花算法生成Id（workerId = 0, datacenterId = 0）
     *
     * @param snowIdMap 重新生成的 snowId 信息 或者 上一次生成的 snowId 信息
     * @return snowId
     */
    public static String calculateAndGetSnowId(@NotNull Map<String, String> snowIdMap) {
        if (CollectionUtil.isEmpty(snowIdMap)) {
            snowIdDTO = new SnowIdDTO();
            snowIdMap.put(snowIdDTO.getIp4Address(), JSON.toJSONString(snowIdDTO));
        } else if (snowIdMap.containsKey(NetUtil.getLocalhostStr())) {
            snowIdDTO = JSON.parseObject(snowIdMap.get(NetUtil.getLocalhostStr()), SnowIdDTO.class);
        } else {
            List<SnowIdDTO> snowIdDTOList = new ArrayList<>();
            // 需要自己先排序，保证list中的数据，根据时间戳从小到大排布
            for (String value : snowIdMap.values()) {
                snowIdDTOList.add(JSON.parseObject(value, SnowIdDTO.class));
            }
            Collections.sort(snowIdDTOList);
            // 节点数据还没用完
            if (snowIdMap.size() < DATA_SIZE * DATA_SIZE) {
                // 当前最后一个节点
                SnowIdDTO currentSnowIdDTO = snowIdDTOList.get(snowIdDTOList.size() - 1);
                // 优先变更工作节点
                if (snowIdDTO.getWorkerId() < DATA_SIZE - 1) {
                    snowIdDTO = new SnowIdDTO(currentSnowIdDTO.getWorkerId() + 1, currentSnowIdDTO.getDataCenterId());
                } else {
                    snowIdDTO = new SnowIdDTO(0, currentSnowIdDTO.getDataCenterId() + 1);
                }
                snowIdMap.put(snowIdDTO.getIp4Address(), JSON.toJSONString(snowIdDTO));
            } else {
                // 超出限制
                return null;
            }
        }
        return getSnowId(snowIdDTO);
    }

    public static SnowIdDTO getSnowIdDTO() {
        return snowIdDTO;
    }

    public static void setSnowIdDTO(SnowIdDTO snowIdDTO) {
        SnowUtil.snowIdDTO = snowIdDTO;
    }

}
