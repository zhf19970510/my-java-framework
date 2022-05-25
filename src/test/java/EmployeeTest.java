import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;

/**
 * @author: 曾鸿发
 * @create: 2022-04-20 17:38
 * @description：
 **/
@RunWith(SpringRunner.class)
public class EmployeeTest {

    @Test
    public void testGenerateEmployee(){
        Employee employee = new Employee();
        employee.setAddress("123");
        employeeAware(employee);
        System.out.println(employee.getAddress());
    }

    public void employeeAware(Employee employee){
        employee.setAddress("456");
    }

    @Test
    public void testSubstract(){
        System.out.println(234578.9722 - 97281.8176);

        BigDecimal bigDecimal = new BigDecimal(234578.9722);
        BigDecimal bigDecimal1 = new BigDecimal(97281.8176);
        BigDecimal bigDecimal2 = bigDecimal.subtract(bigDecimal1).setScale(4, RoundingMode.HALF_UP);
        BigDecimal subtract = bigDecimal.subtract(bigDecimal1);
        System.out.println(bigDecimal2);
        System.out.println(subtract.toPlainString());
        System.out.println(subtract.stripTrailingZeros().toPlainString());
        System.out.println(subtract.toString());
        System.out.println(subtract.toEngineeringString());
    }
}
