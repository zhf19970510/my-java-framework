![img.png](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404042043112.png)

在refresh方法中模板方法模式的体现：

initPropertySource()

postProcessBeanFactory()



BeanFactory中的singletonObjects、singletonFactories、earlySingletonObjects 代表着三级缓存



fresh创建对象，

finshRefresh():

![image-20240405053935753](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404050539152.png)

fresh() -> finishRefresh() -> publishEvent() -> ContextRefreshListener() -> initStrategies(context) 这里开始执行Spring MVC九大内置对象的初始化工作了。

上传组件：multipartResolver。如果定义过当前类型的bean对象，那么直接获取，如果没有的话，可以为null。

LocaleResolver：主要用于处理国际化配置。 需要有独特的视图处理器对应：ResourceBundleViewResolver

... 下面的文件中有对应解析器默认实现

可以通过在自己项目中添加DispatcherServlet.properties，覆盖默认的九大组件实现

![image-20240405093752929](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404050937634.png)

![image-20240405151534465](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404051515847.png)

RequestContextHolder（管理request和session的组件）

RequestContextHolder.getRequestAttributes();



重定向：

FlashMap的相关配置，主要用于Redirect转发时参数的传递，此处有一个应用场景：如果是post请求是提交表单，提交完之后redirect到一个订单的页面，此时需要知道一些订单的信息，但redirect本身没有提交参数的功能，如果想传递参数，那么就必须要写到url.而url有长度的限制，同时还容易对外暴露，此时可以用flashMap来传递参数，对应SpringBoot中就是RedirectAttribute。

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

@Controller
public class MyController {

    @GetMapping("/submitForm")
    public String submitForm(@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        // 在 Flash 属性中添加参数
        redirectAttributes.addFlashAttribute("message", "Hello, " + name + "! This is a flash message.");

        // 重定向到另一个页面
        return "redirect:/result";
    }

    @GetMapping("/result")
    public String result() {
        // 这里不需要做太多处理，因为参数已经通过 Flash 属性传递过来了
        return "result";
    }
}
```

但是在前后端分离的项目中，比较常见的还是使用给前端返回一个对象体，对象体包括返回的参数和url，前端通过this.$router.push(redirectUrl);来进行路由跳转。

service() -> doGet() or doPost() -> doService() -> doDispatch() 

### 上传组件

MultipartFile上传文件是依赖Commons-fileupload

Spring MVC 在 servlet3.0之前是没有默认MultipartResolver的默认实现的。

可以在项目中引入Commons-fileupload，然后就可以使用 CommonsMultipartResolver解析文件了，也就是我们常用的MultipartFile解析文件。如果要自定义一些上传文件的属性，如上传文件最大大小以及文件编码，可以声明一个Bean，来设置属性。

```xml
<dependency>
	<groupId>commons-fileupload</groupId>
	<artifactId>commons-fileupload</artifactId>
	<version>1.3.1</version>
</dependency>
```

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class AppConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        // 在这里可以设置自定义的配置，例如文件大小限制、临时文件存储路径等
        resolver.setMaxUploadSize(1024 * 1024); // 设置上传文件的最大大小为 1MB
        // 其他配置...
        return resolver;
    }
}
```

`invokeAwareMethod` 和 `ApplicationContextAwareProcessor` 是 Spring 框架中用于处理 Aware 接口的方法和类。

1. **invokeAwareMethod**:
   `invokeAwareMethod` 是 Spring 框架中的一个内部方法，用于执行 Bean 实例的 Aware 接口回调。在 Spring 容器创建 Bean 实例后，如果该 Bean 实现了 Aware 接口（如 ApplicationContextAware、BeanFactoryAware、ResourceLoaderAware 等），Spring 容器会调用 `invokeAwareMethod` 方法来执行相应的 Aware 接口回调。这样，Bean 就能获取到对应的环境信息或资源。
2. **ApplicationContextAwareProcessor**:
   `ApplicationContextAwareProcessor` 是 Spring 框架中的一个 Bean 后置处理器（BeanPostProcessor），用于处理实现了 `ApplicationContextAware` 接口的 Bean。它会在 Bean 实例化后，但在 Bean 的初始化回调方法（如 `@PostConstruct`、`afterPropertiesSet()` 等）执行前，为实现了 `ApplicationContextAware` 接口的 Bean 设置对应的 ApplicationContext。

这两者的作用都是为了在 Spring 容器创建 Bean 的过程中，使得 Bean 能够感知到 Spring 容器的相关信息。具体使用方法如下：

```java
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MyBean implements ApplicationContextAware, BeanFactoryAware {

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void doSomething() {
        // 在这里可以使用 applicationContext 和 beanFactory 对象
    }
}
```

在上面的示例中，`MyBean` 类实现了 `ApplicationContextAware` 和 `BeanFactoryAware` 接口，并通过实现对应的方法，获得了对 ApplicationContext 和 BeanFactory 的引用。这样，`MyBean` 就可以在需要时使用这些容器提供的功能了。



在哪里绑定url和ControllerBean：

initializeBean() -> applyBeanPostProcessorBeforeInitialization() -> 遍历beanPostProcessor -> processor.postProcessBeforeInitialization() -> invokeAwareInterfaces() -> setApplicationContext
->initApplicationContext -> detectHandlers() -> registerHandler(urls, beanName) -> 在这里会绑定url和controller



RequestMappingHandleMapping 类似，只不过使用的是initialingBean，在afterProperties()方法中设置的绑定关系。



![image-20240406184322087](C:/Users/admin/AppData/Roaming/Typora/typora-user-images/image-20240406184322087.png)

![image-20240406195342082](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404061953824.png)

initBinder属性编辑器：

![image-20240406214947458](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404062149852.png)

![image-20240406215221654](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/202404062152808.png)

在执行处理器适配器逻辑时，会先获取所有当前Controller所有被@InitBinder修饰的方法都获取到。

对WebDataBinder进行初始化且只对当前的controller有效。

附加ControllAdvice用法：

https://blog.csdn.net/qq_43575801/article/details/128891997



找到全局的以及当前controller的initBinder方法

桥接方法不是用户自定义的方法，而是编译器生成的方法，泛型擦除，兼容jdk1.5以前的版本。



invokeInitMethod -> afterPropertiesSet



获取WebDataBinderFactory和ModelFactory

之后就是将HandlerMethod封装为ServletInvocableHandlerMethod，这个封装类具备了返回值的处理功能并且能够处理@ResponseStatus注解。这是最基本的对象。

给上面的对象进行赋值操作：

1. 设置参数处理器：26个参数解析器
2. 设置返回值处理器：15个返回值处理器
3. 设置DataBinderFactory对象：上面创建好的对象
4. 设置参数名称发现器：3个名称发现器



创建一个ModelAndViewContainer的对象来保存model和view：

在后续处理处理的时候一般使用的都是defaultModel。(BindingAwareModelMap)

​	对ModelAndViewContainer进行属性赋值操作：

		1. 添加重定向请求中参数传递的flashmap属性。
		2. 设置ignoreDefaultModelOnRedirect属性值

基本对象准备好了之后要执行initModel方法来执行某些属性值的赋值工作：

使用modelFactory将sessionAttributes和注释了@ModelAttribute的方法的参数设置到model中。



异步处理：

WebAsyncManager

AsyncWebRequest

![image-20240525075624310](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240525075632.png)

![image-20240525081718439](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240525081718.png)



WebAsyncUtils



SpringMvc怎么用异步的？

获取WebAsyncManager对象，设置拦截器：RequestBindingInterceptor

![image-20240525213400802](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240525213400.png)

![image-20240525213641406](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240525213641.png)

![image-20240525214108379](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240525214108.png)

使用案例：

![image-20240526065328025](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526065328.png)



总结：

![image-20240526074307059](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526074307.png)

![image-20240526080521357](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526080521.png)

![image-20240526081430604](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526081430.png)

![image-20240526083911862](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526083912.png)

![image-20240526084718265](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526084718.png)

![image-20240526154042209](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526154042.png)

![image-20240526162207168](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526162207.png)

准备工作做好之后，开始接收请求：

![image-20240526164843587](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240526164843.png)