package com.zhf.entity.dto;

import cn.hutool.core.net.NetUtil;

import java.io.Serializable;

public class SnowIdDTO implements Serializable, Comparable<SnowIdDTO> {

    private Long timeStamp;

    private long workerId;

    private long dataCenterId;

    private String ip4Address;

    public SnowIdDTO(long workerId, long dataCenterId) {
        this.timeStamp = System.currentTimeMillis();
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.ip4Address = NetUtil.getLocalhostStr();
    }

    public SnowIdDTO() {
        this(0, 0);
    }

    @Override
    public int compareTo(SnowIdDTO o) {
        long ex = this.timeStamp - o.getTimeStamp();
        return ex > 0 ? 1 : -1;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public String getIp4Address() {
        return ip4Address;
    }

    public void setIp4Address(String ip4Address) {
        this.ip4Address = ip4Address;
    }
}
