import com.zhf.superTest.People;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.ClassUtils;

/**
 * @author: 曾鸿发
 * @create: 2021-12-14 20:16
 * @description：测试类程序
 **/
public class MainTest2 {

    private void test1() throws Exception{
        // 反射知识点应用
        Class<Employee> employeeClass = (Class<Employee>) ClassUtils.forName("Employee", this.getClass().getClassLoader());
        // 判断类之间是否有继承关系
        System.out.println(employeeClass.isAssignableFrom(People.class));
        // 实例化对象
        Employee employee = BeanUtils.instantiateClass(employeeClass);
        System.out.println(employee);
        // 获取当前的ClassLoader另外的方法
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(employee);
        beanWrapper.setPropertyValue("empNo", "111");
        System.out.println(employee);
//        ClassUtils.getAllInterfacesForClassAsSet()
    }

    @Test
    public void test2() throws Exception{
        test1();
    }


    public static void main(String[] args) {
        // System.out.println(286.12 * 12);
        // System.out.println(3889.12 * 12);

        // DateUtil.getAllAvailableZoneId();

        System.out.println(500000 / 4);

        System.out.println(4000000 / 4);

        System.out.println((500000 - 200000) / 8);

        System.out.println((4000000 - 1500000) / 8);

        System.out.println(500000 - 200000);

        System.out.println(4000000 - 1500000);
    }
}
