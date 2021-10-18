import com.zhf.MyJavaFrameworkApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: 曾鸿发
 * @create: 2021-09-22 21:08
 * @description：
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class MyJavaFrameworkApplicationTest {
    //
    // @Autowired
    // private MyBeanPostProcessor myBeanPostProcessor;
    //
    // // @Test
    // public void test1(){
    //
    //     ApplicationContext applicationContext = SpringUtil.getApplicationContext();
    //
    //     MyFactoryBean myFactoryBean = applicationContext.getBean(MyFactoryBean.class);
    //     try {
    //         Person object = myFactoryBean.getObject();
    //         System.out.println(object);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

}
