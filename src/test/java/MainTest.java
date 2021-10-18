import com.zhf.entity.Person;
import com.zhf.factory.MyFactoryBean;
import com.zhf.util.DateUtil;
import com.zhf.util.PasswordUtil;
import com.zhf.util.SpringUtil;
import org.springframework.context.ApplicationContext;

/**
 * @author: 曾鸿发
 * @create: 2021-09-14 15:11
 * @description：Main函数测试类
 **/
public class MainTest {

    public static void main(String[] args) {
        // String pad = "@zhf19970510##";
        // System.out.println(PasswordUtil.isContainsReasonablePassword(pad));
        // pad = "@19970510";
        // System.out.println(PasswordUtil.isContainsReasonablePassword(pad));
        // pad = "zhf19970510";
        // System.out.println(PasswordUtil.isContainsReasonablePassword(pad));
        // pad = "Zhf@19970510";
        // System.out.println(PasswordUtil.isContainsReasonablePassword(pad));
        System.out.println(PasswordUtil.isSpecialCharacter("!"));
        System.out.println(PasswordUtil.isSpecialCharacter("@"));
        System.out.println(PasswordUtil.isSpecialCharacter("#"));
        System.out.println(PasswordUtil.isSpecialCharacter("^"));
        System.out.println(PasswordUtil.isSpecialCharacter("%"));
        System.out.println(PasswordUtil.isSpecialCharacter("&"));
        System.out.println(PasswordUtil.isSpecialCharacter("?"));
        System.out.println(PasswordUtil.isSpecialCharacter("$"));
        System.out.println(PasswordUtil.isSpecialCharacter("."));
        System.out.println(PasswordUtil.isSpecialCharacter("["));
        System.out.println(PasswordUtil.isSpecialCharacter("~"));
        System.out.println(PasswordUtil.isSpecialCharacter("{"));
        System.out.println(PasswordUtil.isSpecialCharacter("+"));
        System.out.println(PasswordUtil.isSpecialCharacter("*"));

        System.out.println(DateUtil.checkDateInSection("2023-9", "2021-7", "2022-7"));


        System.out.println("当天开始时间：" + DateUtil.getDayStart());
        System.out.println("当天结束时间：" + DateUtil.getDayEnd());
        System.out.println("当月开始时间：" + DateUtil.getMonthStart());
        System.out.println("当月结束时间：" + DateUtil.getMonthEnd());
        System.out.println("当年开始时间：" + DateUtil.getYearStart());
        System.out.println("当年结束时间：" + DateUtil.getYearEnd());
        System.out.println(DateUtil.getBetweenTimeByFlag("d"));
        System.out.println(DateUtil.getBetweenTimeByFlag("m"));
        System.out.println(DateUtil.getBetweenTimeByFlag("y"));
    }
}
