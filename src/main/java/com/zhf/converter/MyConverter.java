package com.zhf.converter;

import com.zhf.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 00:50
 * @description： 自定义类型转换器
 **/

/**
 * 只有定义好这么一个类型转换器是不够的，还需要将其注入到ConvertServiceFactoryBean中，加入到其中定义的converters集合中，
 * 并且在annotation-driven 对应注解中指明具体的conversion-service <mvc:annotation-driven conversion-service="conversionService">
 */
@Component
public class MyConverter implements Converter<String, User> {

    @Override
    public User convert(String source) {
        User user = null;
        if(StringUtils.isNotBlank(source) && source.split("-").length == 4){
            user = new User();
            user.setId(Integer.parseInt(source.split("-")[0]));
            user.setUsername(source.split("-")[1]);
            user.setAge(Integer.parseInt(source.split("-")[2]));
            user.setPassword(source.split("-")[3]);
        }
        return user;
    }
}
