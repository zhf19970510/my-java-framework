package com.zhf.step;

import java.util.List;

public interface SubGroupInterface<T> {

    void addSubGroup(T item);

    List<T> getSubGroupItems();

    void getSubGroupItems(List<T> subGroupItems);

}
