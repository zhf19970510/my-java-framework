package com.zhf.design.pattern.iterator;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ConcreteIterator<E> implements Iterator{

    // 游标
    private int cursor;

    private ArrayList<E> arrayList;

    public ConcreteIterator(ArrayList<E> arrayList) {
        this.cursor = 0;
        this.arrayList = arrayList;
    }

    @Override
    public boolean hasNext() {
        return cursor != arrayList.size();
    }

    @Override
    public void next() {
        cursor++;
    }

    @Override
    public Object currentItem() {
        if(cursor >= arrayList.size()) {
            throw new NoSuchElementException();
        }
        return arrayList.get( cursor);
    }
}
