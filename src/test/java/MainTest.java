import com.zhf.util.DateUtil;

/**
 * @author: 曾鸿发
 * @create: 2021-09-14 15:11
 * @description：Main函数测试类
 **/
public class MainTest {

    public static void main(String[] args) {
        String weekStartTime = DateUtil.getWeekStartTime();
        String weekEndTime = DateUtil.getWeekEndTime();
        System.out.println(weekStartTime);
        System.out.println(weekEndTime);
    }
}
