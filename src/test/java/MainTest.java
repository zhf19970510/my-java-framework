import com.zhf.util.DateUtil;
import com.zhf.util.PasswordUtil;

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

    }
}
