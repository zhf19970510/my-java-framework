package com.zhf.service.impl;

import cn.hutool.core.io.StreamProgress;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class StreamProgressImpl implements StreamProgress {
    private final String fileName;

    @Override
    public void start() {
        log.info("start progress {}", fileName);
    }

    @Override
    public void progress(long total, long progressSize) {
        log.debug("progress {}, total = {}, progressSize = {}", fileName, total, progressSize);
    }

    @Override
    public void finish() {
        log.info("finish progress {}", fileName);
    }
}
