package com.zhf.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExcelListenerUtil<T> extends AnalysisEventListener<T> {

    private final List<T> dataList = new ArrayList<>();

    public List<T> getDataList() {
        return this.dataList;
    }

    @Override
    public void invoke(T var1, AnalysisContext analysisContext) {
        this.dataList.add(var1);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
