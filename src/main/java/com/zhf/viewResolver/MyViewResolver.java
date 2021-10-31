package com.zhf.viewResolver;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 00:01
 * @description： 自定义视图解析器
 **/

@Component
/**
 * 通过实现Ordered接口，可以自定义加载的顺序，值越大越先执行
 * @return
 */
@Order(1)
public class MyViewResolver implements ViewResolver, Ordered {

    private int order = 3;

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {

        if(viewName.startsWith("msb:")){
            System.out.println(viewName);
            return new MyView();
        }else{
            return null;
        }
    }

    @Override
    public int getOrder() {
        return order;
    }
}
