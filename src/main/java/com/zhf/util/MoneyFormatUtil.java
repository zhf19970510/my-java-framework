package com.zhf.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatUtil {

    public String formatMoney(Object obj){
        NumberFormat decimalFormat = new DecimalFormat("#,##0.0");
        return decimalFormat.format(obj);
    }

    public String formatMoneyWithSymbol(Locale locale, Object obj){
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(locale);
        return currencyInstance.format(obj);
    }
}
