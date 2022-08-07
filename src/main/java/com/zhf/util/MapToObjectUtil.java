package com.zhf.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;

import java.util.Date;
import java.util.Map;

/**
 * 该类主要一个作用是将含有日期类型的Map转换为 Bean
 * 如果要将 Bean转换为 Map，因为效率更高，参考：
 * @see com.zhf.util.MyBeanUtils#objectToMap(java.lang.Object)
 */
public class MapToObjectUtil {

    private static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();

    static  {
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
        convertUtilsBean.register(dateConverter, Date.class);
    }

    // 等会试一下加不加 new PropertyUtilsBean() 的作用和效果
    private static BeanUtilsBean describeBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());

    public static Map<String, String> objToMap(Object bean) throws Exception{
        return describeBean.describe(bean);
    }

    public static  <T> T transFormAnotherBean(Map<String, Object> source, Class<T> clazz) throws Exception{
        T t = clazz.newInstance();
        describeBean.populate(t, source);
        return t;
    }

}
