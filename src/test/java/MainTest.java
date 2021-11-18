import com.zhf.entity.Person;
import com.zhf.entity.User;
import com.zhf.factory.MyFactoryBean;
import com.zhf.util.DateUtil;
import com.zhf.util.PasswordUtil;
import com.zhf.util.SpringUtil;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println(new Date());
        Date dateBeforeSec = DateUtil.getDateBeforeSec(new Date(), 15);
        System.out.println(dateBeforeSec);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println(simpleDateFormat.parse("2021-10-09 00:00:00").getTime());
            System.out.println(simpleDateFormat.parse("2021-10-10 23:00:00").getTime());
            Date date = new Date(1633846729162L);
            System.out.println(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String s = "ycwlwg03.YX031074";
        String pointName = s.substring(s.indexOf(".") + 1);
        System.out.println(pointName);

        User user = new User();
        user.setUsername("zhf");
        user.setPassword("123456");
        user.setId(1);
        List<User> users = new ArrayList<>();
        users.add(user);
        for(int i = 0; i < 5000; i++){
            user = new User();
            user.setUsername("lhl" + i);
            user.setPassword("56789");
            user.setId(1);
            users.add(user);
        }
        for (User user1 : users) {
            System.out.println(user1);
        }
        long startTime = System.currentTimeMillis();
        List<User> collect = users.stream().filter(t -> !"zhf".equals(t.getUsername())).collect(Collectors.toList());
        System.out.println("耗费" + (System.currentTimeMillis() - startTime) + "毫秒");
        System.out.println(collect);

        String name = "zhf";
        name += "\t" + "lhl";
        System.out.println(name);

        List<String> userNames = users.stream().filter(t -> t.getPassword().equals("56789")).filter(t -> t.getUsername().contains("lhl1881")).map(User::getUsername).collect(Collectors.toList());
        userNames.forEach(t -> System.out.println(t));
        List<String> pointNum = new ArrayList<>();
        pointNum.add("111");
        pointNum.add("2222");
        pointNum.add("111");
        pointNum.add("3333");
        Set<String> pointNumSet = new HashSet<>();
        pointNumSet.add("2222");
        pointNumSet.add("4444");
        pointNumSet.add("5555");
        pointNumSet.add("3333");
        List<String> allAlarmPointNumber = getAllAlarmPointNumber(pointNum, pointNumSet);
        System.out.println(allAlarmPointNumber);

        String s1 = "zhf\t11";
        String[] split = s1.split("\t");
        for (String s2 : split){
            System.out.println("字符串是：" + s2 + "!");
        }

        Date date1 = new Date(1633863682138L);
        System.out.println(date1);
        Date date2 = new Date(1633863671949L);
        System.out.println(date2);

        // Date date3 = new Date(1635217);

        String tim1 = "2021-10-29 10:35:40";
        String iime2 = "2021-10-29 10:35:55";

        String time4 = "2021-10-29 17:11:29";

        String time5 = "2021-10-29 17:11:20";
        String time6 = "2021-10-29 17:11:35";

        // Date date4 = new Date(2021-10-29 17:11:29);

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println(simpleDateFormat1.parse(tim1).getTime());
            System.out.println(simpleDateFormat1.parse(iime2).getTime());
            System.out.println(simpleDateFormat1.parse(time4).getTime());
            // 1635498680000
            System.out.println(simpleDateFormat1.parse(time5).getTime());
            // 1635498695000
            System.out.println(simpleDateFormat1.parse(time6).getTime());

            String time7 = "2021-10-30 00:00:00";
            String time8 = "2021-10-30 23:59:59";
            System.out.println(simpleDateFormat1.parse(time7).getTime());
            System.out.println(simpleDateFormat1.parse(time8).getTime());

            System.out.println("2021-11-01 00:00:00");

            String time9 = "2021-11-01 00:00:00";
            String time10 = "2021-11-01 23:59:59";
            System.out.println(simpleDateFormat1.parse(time9).getTime());
            System.out.println(simpleDateFormat1.parse(time10).getTime());

            String time11 = "2021-11-03 00:00:00";
            String time12 = "2021-11-03 23:59:59";
            System.out.println(simpleDateFormat1.parse(time11).getTime());
            System.out.println(simpleDateFormat1.parse(time12).getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("修改设备：");
        String s2 = null;
        stringBuffer.append(s2);
        String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
        System.out.println(substring);
        //
        // Map<String, List<User>> map = new HashMap<>();
        // List<User> users1 = new ArrayList<>();
        // users1.add( new User(1, "111", "111", 1));
        // users1.add(new User(2, "111", "222", 2));
        // map.put("111", users1);
        // List<User> users2 = new ArrayList<>();
        // users2.add(new User(3, "222", "222", 3));
        // users2.add(new User(4, "222", "333", 4));
        // map.put("222", users2);
        // Set<Map.Entry<String, List<User>>> entries = map.entrySet();
        // for (Map.Entry<String, List<User>> entry : entries){
        //     System.out.println(entry.getKey() + " : " + entry.getValue());
        // }
        //
        // // 试试可不可以将可以替换
        // if(map.containsKey("111")){
        //     List<User> users3 = map.get("111");
        //     users3.stream().forEach(t -> t.setUsername("333"));
        //     map.put("333", users3);
        //     map.remove("111");
        // }
        //
        // for (Map.Entry<String, List<User>> entry : entries){
        //     System.out.println(entry.getKey() + " : " + entry.getValue());
        // }


        List<String> infos = new ArrayList<>();
        infos.add("aaa");
        infos.add("bbb");
        List<String> collect1 = infos.stream().filter(t -> "ccc".equals(t)).collect(Collectors.toList());
        System.out.println(collect1);
        System.out.println(collect1.size());
        for (String tmp : collect1){
            System.out.println(tmp);
        }
        System.out.println("aaa");

        String offer_id ="";
        offer_id +="    #"+"9015096";
        offer_id = offer_id.trim();

        String system = "动环系统";
        system = system.replace("系统", "");
        System.out.println(system);
    }

    private static List<String> getAllAlarmPointNumber(List<String> alarmPointList, Set<String> newAlarmPointNums) {
        HashSet<String> pointNumSet = new HashSet<>();
        pointNumSet.addAll(alarmPointList);
        pointNumSet.addAll(newAlarmPointNums);
        return new ArrayList<>(pointNumSet);
    }
}
