package com.zhf.util.pinin;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class PinInLocaleComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        return Collator.getInstance(Locale.CHINESE).compare(s1, s2);
    }
}
