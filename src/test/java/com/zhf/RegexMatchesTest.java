package com.zhf;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.zhf.util.DateRegexProcessor;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: 曾鸿发
 * @create: 2022-06-11 08:11
 * @description：
 **/
public class RegexMatchesTest {

    @Test
    public void testRegexMatches1() {
        String content = "I am noob " +
                "from runoob.com.";

        String pattern = ".*runoob.*";

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);
    }

    @Test
    public void testRegexMatches2() {
        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(\\D*)(\\d+)(.*)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(3) );
        } else {
            System.out.println("NO MATCH");
        }
    }

    @Test
    public void testRegexMatches3(){

        final String REGEX = "\\bcat\\b";
        final String INPUT =
                "cat cat cat cattie cat";

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT); // 获取 matcher 对象
        int count = 0;

        while(m.find()) {
            count++;
            System.out.println("Match number "+count);
            System.out.println("start(): "+m.start());
            System.out.println("end(): "+m.end());
        }
    }

    @Test
    public void testRegexMatches4(){
        final String REGEX = "foo";
        final String INPUT = "fooooooooooooooooo";
        final String INPUT2 = "ooooofoooooooooooo";
        Pattern pattern;
        Matcher matcher;
        Matcher matcher2;
        pattern = Pattern.compile(REGEX);
        matcher = pattern.matcher(INPUT);
        matcher2 = pattern.matcher(INPUT2);

        System.out.println("Current REGEX is: "+REGEX);
        System.out.println("Current INPUT is: "+INPUT);
        System.out.println("Current INPUT2 is: "+INPUT2);


        System.out.println("lookingAt(): "+matcher.lookingAt());
        System.out.println("matches(): "+matcher.matches());
        System.out.println("lookingAt(): "+matcher2.lookingAt());
    }

    @Test
    public void testRegexMatches5(){
        String regex1 = "(\\d{4})/(\\d{2})/(\\d{2})";
        String now = "2022/06/11";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(now);
        StringBuffer stringBuffer = new StringBuffer();
        int i = 1;
        if(Pattern.matches(regex1, now)){
            if (matcher.find()){
                stringBuffer.append(matcher.group(1));
                stringBuffer.append(matcher.group(2));
                stringBuffer.append(matcher.group(3));
            }
        }
        System.out.println(stringBuffer.toString());



        System.out.println(90000 / 60);   //

        System.out.println(55000 / 60);   // 1205

        System.out.println((28000 + 90000 + 55000) / 60);             //

        // 最坏情况 36 期 4800

        // 最好 60期 2900

        // 分期乐 39000

        // 有钱花  42000

        // 借呗 7500

        // 京东金融 6600

        {
            //
            System.out.println(3 * 817 + 3890 * 10);

            System.out.println(28000 + 90000 + 55000);

            System.out.println(39000 + 42000 + 7500 + 6600);

            // 网贷 最坏 8000

            // 网贷 最好

            // 最好： 2900
            // 要存
            // 两个月 借呗
            // 两个月 白条

            // 80000 / 12 = 7000

            System.out.println(260000 * 0.04);

            // 3181 + 7423 / 4 =
            System.out.println();

        }

        {
            String s = "aaa(bbb)ccc";
            String regex11 = "*\\(*\\)*";
            Pattern pattern1 = Pattern.compile(regex1);
            Matcher matcher1 = pattern1.matcher(s);
            System.out.println(matcher1.matches());
        }


    }



    @Test
    public void testDataRegexProcessor(){
        // ,dd-MM-yyyy,yyyyMMdd,yyyy/M/d,M/d/yyyy,d/M/yyyy,MM/d/yyyy,dd/M/yyyy
        String targetFormat = "yyyyMMdd";
        {
            // yyyy/MM/dd
            String date = "2022/06/11";
            String s = DateRegexProcessor.processDate(date, "yyyy/MM/dd", targetFormat);
            System.out.println("yyyy/MM/dd 转换后：" + s);
        }

        {
            // MM/dd/yyyy
            String date = "06/11/2022";
            String s = DateRegexProcessor.processDate(date, "MM/dd/yyyy", targetFormat);
            System.out.println("MM/dd/yyyy 转换后：" + s);
        }

        {
            // dd/MM/yyyy
            String date = "11/06/2022";
            String s = DateRegexProcessor.processDate(date, "dd/MM/yyyy", targetFormat);
            System.out.println("dd/MM/yyyy 转换后：" + s);
        }

        {
            // yyyy-MM-dd
            String date = "2022-06-11";
            String s = DateRegexProcessor.processDate(date, "yyyy-MM-dd", targetFormat);
            System.out.println("yyyy-MM-dd 转换后：" + s);
        }

        {
            // MM-dd-yyyy
            String date = "06-11-2022";
            String s = DateRegexProcessor.processDate(date, "MM-dd-yyyy", targetFormat);
            System.out.println("MM-dd-yyyy 转换后：" + s);
        }

        // 后面就不测了，因为我发现这个是有漏洞的...

        {
            System.out.println(12900 - 2000 - 1073 - 4000 - 817 - 1060 - 2290 - 455);
        }

    }

    @Test
    public void testMyRegex(){
        {
            String s = "-33";
            String regex22 = "^[1-9]\\d*$";
            Pattern pattern1 = Pattern.compile(regex22);
            Matcher matcher1 = pattern1.matcher(s);
            System.out.println(matcher1.matches());
        }
    }

}
